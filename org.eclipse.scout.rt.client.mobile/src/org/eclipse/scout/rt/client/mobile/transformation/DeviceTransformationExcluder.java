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
package org.eclipse.scout.rt.client.mobile.transformation;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;

/**
 * @since 3.9.0
 */
public class DeviceTransformationExcluder {
  private static final IScoutLogger LOG = ScoutLogManager.getLogger(DeviceTransformationExcluder.class);

  private Map<IForm, ExclusionInfo> m_excludedForms = new WeakHashMap<IForm, ExclusionInfo>();
  private Map<IFormField, ExclusionInfo> m_excludedFields = new WeakHashMap<IFormField, ExclusionInfo>();

  public void excludeForm(IForm form) {
    ExclusionInfo exclusionInfo = m_excludedForms.get(form);
    if (exclusionInfo == null) {
      exclusionInfo = new ExclusionInfo();
      m_excludedForms.put(form, exclusionInfo);
    }

    exclusionInfo.setExcludeAllTransformations(true);

    LOG.debug("Excluding form " + form);
  }

  public void excludeFormTransformation(IForm form, MobileDeviceTransformation transformation) {
    ExclusionInfo exclusionInfo = m_excludedForms.get(form);
    if (exclusionInfo == null) {
      exclusionInfo = new ExclusionInfo();
      m_excludedForms.put(form, exclusionInfo);
    }

    exclusionInfo.getExcludedTransformations().add(transformation);

    LOG.debug("Excluding form transformation " + transformation + " for form " + form);
  }

  public boolean isFormExcluded(IForm form) {
    ExclusionInfo exclusionInfo = m_excludedForms.get(form);
    if (exclusionInfo == null) {
      return false;
    }

    if (exclusionInfo.isExcludeAllTransformations()) {
      return true;
    }

    return false;
  }

  public boolean isFormTransformationExcluded(IForm form, MobileDeviceTransformation transformation) {
    ExclusionInfo exclusionInfo = m_excludedForms.get(form);
    if (exclusionInfo == null) {
      return false;
    }

    if (exclusionInfo.isExcludeAllTransformations() || exclusionInfo.getExcludedTransformations().contains(transformation)) {
      return true;
    }

    return false;
  }

  public void excludeField(IFormField formField) {
    ExclusionInfo exclusionInfo = m_excludedFields.get(formField);
    if (exclusionInfo == null) {
      exclusionInfo = new ExclusionInfo();
      m_excludedFields.put(formField, exclusionInfo);
    }

    exclusionInfo.setExcludeAllTransformations(true);

    LOG.debug("Excluding field " + formField);
  }

  public void excludeFieldTransformation(IFormField formField, MobileDeviceTransformation transformation) {
    ExclusionInfo exclusionInfo = m_excludedFields.get(formField);
    if (exclusionInfo == null) {
      exclusionInfo = new ExclusionInfo();
      m_excludedFields.put(formField, exclusionInfo);
    }

    exclusionInfo.getExcludedTransformations().add(transformation);

    LOG.debug("Excluding field transformation " + transformation + " for field " + formField);
  }

  public boolean isFieldExcluded(IFormField formField) {
    ExclusionInfo exclusionInfo = m_excludedFields.get(formField);
    if (exclusionInfo == null) {
      return false;
    }

    if (exclusionInfo.isExcludeAllTransformations()) {
      return true;
    }

    return false;
  }

  public boolean isFieldTransformationExcluded(IFormField formField, MobileDeviceTransformation transformation) {
    ExclusionInfo exclusionInfo = m_excludedFields.get(formField);
    if (exclusionInfo == null) {
      return false;
    }

    if (exclusionInfo.isExcludeAllTransformations() || exclusionInfo.getExcludedTransformations().contains(transformation)) {
      return true;
    }

    return false;
  }

  private class ExclusionInfo {
    private boolean m_excludeAllTransformations = false;
    private Set<MobileDeviceTransformation> m_excludedTransformations;

    public ExclusionInfo() {
      m_excludedTransformations = new HashSet<MobileDeviceTransformation>();
    }

    public Set<MobileDeviceTransformation> getExcludedTransformations() {
      return m_excludedTransformations;
    }

    public boolean isExcludeAllTransformations() {
      return m_excludeAllTransformations;
    }

    public void setExcludeAllTransformations(boolean excludeAllTransformations) {
      m_excludeAllTransformations = excludeAllTransformations;
    }
  }
}
