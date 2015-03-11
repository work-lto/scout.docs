/*******************************************************************************
 * Copyright (c) 2010 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package org.eclipse.scout.rt.server.services.common.clustersync;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.security.auth.Subject;

import org.eclipse.scout.commons.Assertions;
import org.eclipse.scout.commons.ConfigIniUtility;
import org.eclipse.scout.commons.EventListenerList;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.job.IRunnable;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.commons.security.SimplePrincipal;
import org.eclipse.scout.rt.platform.cdi.OBJ;
import org.eclipse.scout.rt.server.job.IServerJobManager;
import org.eclipse.scout.rt.server.job.ServerJobInput;
import org.eclipse.scout.rt.server.services.common.clustersync.internal.ClusterNotificationMessage;
import org.eclipse.scout.rt.server.services.common.clustersync.internal.ClusterNotificationMessageProperties;
import org.eclipse.scout.rt.server.session.ServerSessionProvider;
import org.eclipse.scout.rt.server.session.ServerSessionProviderWithCache;
import org.eclipse.scout.rt.server.transaction.AbstractTransactionMember;
import org.eclipse.scout.rt.server.transaction.ITransaction;
import org.eclipse.scout.service.AbstractService;
import org.eclipse.scout.service.IService;
import org.eclipse.scout.service.SERVICES;

public class ClusterSynchronizationService extends AbstractService implements IClusterSynchronizationService, IPublishSubscribeMessageListener {
  private static final String TRANSACTION_MEMBER_ID = ClusterSynchronizationService.class.getName();
  private static final IScoutLogger LOG = ScoutLogManager.getLogger(ClusterSynchronizationService.class);
  private static final String PROP_USER_CLUSTER_SYNC = String.format("%s#user", ClusterSynchronizationService.class.getName());
  private static final String CLUSTER_NODE_ID_PARAM = "org.eclipse.scout.rt.server.clusterNodeId";

  private final EventListenerList m_listenerList = new EventListenerList();
  private final ClusterNodeStatusInfo m_statusInfo = new ClusterNodeStatusInfo();

  // variables must not be synchronized as they are only set at initialization
  private String m_nodeId;
  private final Subject m_subject;

  private volatile boolean m_enabled;
  private volatile IPublishSubscribeMessageService m_messageService;

  public ClusterSynchronizationService() {
    m_subject = new Subject();
    m_subject.getPrincipals().add(new SimplePrincipal(ConfigIniUtility.getProperty(PROP_USER_CLUSTER_SYNC, "cluster-synchronization")));
    m_subject.setReadOnly();
  }

  @Override
  public void initializeService() {
    super.initializeService();
    m_nodeId = createNodeId();
  }

  protected String createNodeId() {
    // system property defined node id
    String nodeId = ConfigIniUtility.getProperty(CLUSTER_NODE_ID_PARAM);

    // weblogic name as node id
    if (!StringUtility.hasText(nodeId)) {
      nodeId = System.getProperty("weblogic.Name");
    }

    // jboss node name as node id
    if (!StringUtility.hasText(nodeId)) {
      nodeId = System.getProperty("jboss.node.name");
    }

    // use host name
    if (!StringUtility.hasText(nodeId)) {
      String hostname;
      try {
        hostname = InetAddress.getLocalHost().getHostName();
      }
      catch (UnknownHostException e) {
        hostname = null;
      }
      // might result in a hostname 'localhost'
      if (StringUtility.isNullOrEmpty(hostname) || "localhost".equals(hostname)) {
        // use random number
        nodeId = UUID.randomUUID().toString();
      }

      // in development on same machine there might run multiple instances on different ports (usecase when testing cluster sync)
      // therefore we use in this case the port jetty port too
      String port = ConfigIniUtility.getProperty("org.eclipse.equinox.http.jetty.http.port");
      nodeId = StringUtility.join(":", hostname, port);
    }
    return nodeId;
  }

  protected EventListenerList getListenerList() {
    return m_listenerList;
  }

  @Override
  public void addListener(IClusterNotificationListener listener) {
    m_listenerList.add(IClusterNotificationListener.class, listener);
  }

  @Override
  public void removeListener(IClusterNotificationListener listener) {
    m_listenerList.remove(IClusterNotificationListener.class, listener);
  }

  protected IClusterNotificationListener[] getListeners() {
    return getListenerList().getListeners(IClusterNotificationListener.class);
  }

  @Override
  public ClusterNodeStatusInfo getClusterNodeStatusInfo() {
    return m_statusInfo;
  }

  @Override
  public String getNodeId() {
    return m_nodeId;
  }

  protected IPublishSubscribeMessageService getMessageService() {
    return m_messageService;
  }

  protected void setMessageService(IPublishSubscribeMessageService messageService) {
    m_messageService = messageService;
  }

  protected void setEnabled(boolean enabled) {
    m_enabled = enabled;
  }

  @Override
  public boolean isEnabled() {
    return m_enabled;
  }

  /**
   * Contributing all {@link IClusterNotificationListenerService} to the listenerList. If a listener was already added,
   * removes old listener if there is now a service with a higher ranking.
   */
  protected void contributeListeners() throws ProcessingException {
    Set<IClusterNotificationListener> currentListeners = new HashSet<IClusterNotificationListener>(Arrays.asList(getListeners()));

    for (IClusterNotificationListenerService contributingService : SERVICES.getServices(IClusterNotificationListenerService.class)) {
      IService definingService = SERVICES.getService(contributingService.getDefiningServiceInterface());
      if (contributingService == definingService) {
        if (!currentListeners.contains(contributingService)) {
          addListener(contributingService.getClusterNotificationListener());
        }
      }
      else {
        if (currentListeners.contains(contributingService)) {
          removeListener(contributingService.getClusterNotificationListener());
        }
      }
    }
  }

  @Override
  public boolean enable() {
    if (isEnabled()) {
      return true;
    }
    if (getNodeId() == null) {
      LOG.error("Clustersync could not be enabled. No cluster nodeId could be determined.");
      return false;
    }
    IPublishSubscribeMessageService messageService = SERVICES.getService(IPublishSubscribeMessageService.class);
    if (messageService == null) {
      LOG.error("Clustersync could not be enabled. No MessageService found.");
      return false;
    }
    try {
      messageService.setListener(this);
      messageService.subscribe();
      setMessageService(messageService);
      contributeListeners();
    }
    catch (Exception e) {
      LOG.error(e.getMessage(), e);
      return false;
    }
    setEnabled(true);
    return true;
  }

  @Override
  public boolean disable() {
    if (!isEnabled()) {
      return true;
    }
    setEnabled(false);
    IPublishSubscribeMessageService messageService = getMessageService();
    if (messageService != null) {
      try {
        messageService.unsubsribe();
      }
      catch (Exception e) {
        LOG.error(e.getMessage(), e);
        return false;
      }
    }
    return true;
  }

  @Override
  public void publishNotification(IClusterNotification notification) throws ProcessingException {
    if (isEnabled()) {
      ClusterNotificationMessage message = new ClusterNotificationMessage(notification, getNotificationProperties());
      getTransaction().addMessage(message);
    }
  }

  protected IClusterNotificationMessageProperties getNotificationProperties() {
    return new ClusterNotificationMessageProperties(getNodeId(), ServerSessionProvider.currentSession().getUserId());
  }

  protected void notifyListeners(IClusterNotificationMessage message) {
    for (IClusterNotificationListener listener : getListeners()) {
      try {
        listener.onNotification(message);
      }
      catch (Exception e) {
        LOG.error(String.format("Failed to notify listener about cluster-sync-message [message=%s, listener=%s]", message, listener.getClass().getName()), e);
      }
    }
  }

  @Override
  public void onMessage(final IClusterNotificationMessage message) throws ProcessingException {
    //Do not progress notifications sent by node itself
    String originNode = message.getProperties().getOriginNode();
    if (getNodeId().equals(originNode)) {
      return;
    }

    getClusterNodeStatusInfo().updateReceiveStatus(message);

    ServerJobInput jobInput = ServerJobInput.empty();
    jobInput.name("cluster-sync-receive");
    jobInput.subject(m_subject);
    jobInput.session(OBJ.one(ServerSessionProviderWithCache.class).provide(jobInput.copy()));

    OBJ.one(IServerJobManager.class).runNow(new IRunnable() {

      @Override
      public void run() throws Exception {
        notifyListeners(message);
      }
    }, jobInput);
  }

  @Override
  public void disposeServices() {
    super.disposeServices();
    disable();
  }

  protected ClusterSynchronizationTransaction getTransaction() throws ProcessingException {
    ITransaction tx = Assertions.assertNotNull(ITransaction.CURRENT.get(), "Transaction required");
    ClusterSynchronizationTransaction m = (ClusterSynchronizationTransaction) tx.getMember(TRANSACTION_MEMBER_ID);
    if (m == null) {
      m = new ClusterSynchronizationTransaction(TRANSACTION_MEMBER_ID, getMessageService());
      tx.registerMember(m);
    }
    return m;
  }

  /**
   * Transaction member that notifies other cluster nodes after the causing Scout transaction has been committed. This
   * ensures that other cluster nodes are not informed too early.
   */
  protected static class ClusterSynchronizationTransaction extends AbstractTransactionMember {
    private final List<IClusterNotificationMessage> m_messageQueue;
    private final IPublishSubscribeMessageService m_messageService;

    public ClusterSynchronizationTransaction(String transactionId, IPublishSubscribeMessageService messageService) throws ProcessingException {
      super(transactionId);
      m_messageQueue = new LinkedList<IClusterNotificationMessage>();
      m_messageService = messageService;
    }

    public synchronized void addMessage(IClusterNotificationMessage m) {
      // check if new message can be merged with existing or if it is replacing a new one
      for (Iterator<IClusterNotificationMessage> it = m_messageQueue.iterator(); it.hasNext();) {
        IClusterNotificationMessage existingElem = it.next();
        if (existingElem.coalesce(m)) {
          it.remove();
        }
      }
      m_messageQueue.add(m);
    }

    @Override
    public boolean commitPhase1() {
      return true;
    }

    @Override
    public synchronized void commitPhase2() {
      m_messageService.publishNotifications(new ArrayList<IClusterNotificationMessage>(m_messageQueue));
    }

    @Override
    public synchronized boolean needsCommit() {
      return !m_messageQueue.isEmpty();
    }

    @Override
    public void release() {
    }

    @Override
    public synchronized void rollback() {
      m_messageQueue.clear();
    }
  }
}
