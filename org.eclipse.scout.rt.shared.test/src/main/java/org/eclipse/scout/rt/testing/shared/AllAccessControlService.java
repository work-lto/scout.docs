/*******************************************************************************
 * Copyright (c) 2013 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/

package org.eclipse.scout.rt.testing.shared;

import java.security.AccessController;
import java.security.AllPermission;
import java.security.Permission;
import java.security.Permissions;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.security.auth.Subject;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;
import org.eclipse.scout.rt.shared.services.common.security.IAccessControlService;

/**
 * {@link AllAccessControlService} service for testing using {@link AllPermission}
 * <p>
 * This service is ignored by default and needs to be registered explicitly.
 * </p>
 */
@Order(4500)
public class AllAccessControlService implements IAccessControlService {
  private final Permissions m_permSet;

  public AllAccessControlService() {
    m_permSet = new Permissions();
    m_permSet.add(new AllPermission());
  }

  @Override
  public boolean checkPermission(Permission p) {
    if (p == null) {
      return true;
    }
    Permissions c = getPermissions();
    if (c == null) {
      return true;
    }
    else {
      return c.implies(p);
    }
  }

  @Override
  public int getPermissionLevel(Permission p) {
    if (p == null) {
      return BasicHierarchyPermission.LEVEL_NONE;
    }
    if (!(p instanceof BasicHierarchyPermission)) {
      if (checkPermission(p)) {
        return BasicHierarchyPermission.LEVEL_ALL;
      }
      else {
        return BasicHierarchyPermission.LEVEL_NONE;
      }
    }
    BasicHierarchyPermission hp = (BasicHierarchyPermission) p;
    Permissions c = getPermissions();
    if (c == null) {
      List<Integer> levels = hp.getValidLevels();
      return levels.get(levels.size() - 1);
    }
    else {
      int maxLevel = BasicHierarchyPermission.LEVEL_UNDEFINED;
      Enumeration<Permission> en = c.elements();
      while (en.hasMoreElements()) {
        Permission grantedPermission = en.nextElement();
        if (grantedPermission instanceof BasicHierarchyPermission) {
          BasicHierarchyPermission hgrantedPermission = (BasicHierarchyPermission) grantedPermission;
          if (hgrantedPermission.getClass().isAssignableFrom(hp.getClass())) {
            maxLevel = Math.max(maxLevel, hgrantedPermission.getLevel());
            if (maxLevel >= BasicHierarchyPermission.LEVEL_ALL) {
              break;
            }
          }
        }
      }
      return maxLevel;
    }
  }

  @Override
  public Permissions getPermissions() {
    return m_permSet;
  }

  @Override
  public boolean isProxyService() {
    return false;
  }

  @Override
  public void clearCache() {
  }

  @Override
  public void clearCacheOfUserIds(Collection<String> userIds) {
  }

  @Override
  public String getUserIdOfCurrentSubject() {
    Subject subject = Subject.getSubject(AccessController.getContext());
    if (subject == null || subject.getPrincipals().isEmpty()) {
      return null;
    }
    else {
      return subject.getPrincipals().iterator().next().getName();
    }
  }
}
