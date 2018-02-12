/*******************************************************************************
 * Copyright (c) 2018 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package org.eclipse.scout.rt.jackson.dataobject;

import java.util.Map;

import org.eclipse.scout.rt.platform.dataobject.DoEntity;

/**
 * Base type for data objects with {@link Map}-like structure using key type {@link String} and generic value type
 * {@code T}
 * <p>
 * Attribute definition methods for attributes of map value type {@code T} may be omitted, since all attributes without
 * a specific type definition using an accessor method are assumed to be of type {@code T}.
 * <p>
 * Example map data object using {@code AnotherDo} as map-value type and specifying an additional {@link Integer}
 * attribute {@code count}.
 *
 * <pre>
 * &#64;TypeName("ExampleEntity")
 * public class ExampleMapDo extends DoMap&lt;AnotherDo&gt; {
 *
 *   public DoValue&lt;Integer&gt; count() {
 *     return doValue("count");
 *   }
 *
 * }
 * </pre>
 *
 * @see DoEntity Examples for declaring typed attributes on {@link DoEntity}
 */
public class DoMapEntity<T> extends DoEntity {

  @Override
  @SuppressWarnings("unchecked")
  public T get(String attributeName) {
    return (T) super.get(attributeName);
  }
}
