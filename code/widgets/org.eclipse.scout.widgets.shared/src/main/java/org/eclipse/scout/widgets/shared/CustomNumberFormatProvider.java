/*
 * Copyright (c) 2010, 2023 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.scout.widgets.shared;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.platform.util.NumberFormatProvider;
import org.eclipse.scout.rt.platform.util.ObjectUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Replace
public class CustomNumberFormatProvider extends NumberFormatProvider {

  private static final Logger LOG = LoggerFactory.getLogger(CustomNumberFormatProvider.class);

  private final Set<Locale> m_customLocales;
  private final Map<String, Locale> m_countryDefaultLocaleMap;
  private final Locale[] m_availableLocales;

  public CustomNumberFormatProvider() {
    m_customLocales = new HashSet<>();
    m_countryDefaultLocaleMap = new HashMap<>();

    // add locale support for en_CH
    init(new Locale("en", "CH"), "de");
    // add locale support for en_DE
    init(new Locale("en", "DE"), "de");
    // add locale support for en_AT
    init(new Locale("en", "AT"), "de");
    // add locale support for en_FR
    init(new Locale("en", "FR"), "fr");
    // add locale support for en_IT
    init(new Locale("en", "IT"), "it");
    // add locale support for en_DK
    init(new Locale("en", "DK"), "da");
    // add locale support for en_ES
    init(new Locale("en", "ES"), "es");

    // available locales
    HashSet<Locale> availableLocales = new HashSet<>();
    availableLocales.addAll(m_customLocales);
    availableLocales.addAll(Arrays.asList(super.getAvailableLocales()));
    m_availableLocales = availableLocales.toArray(new Locale[0]);
  }

  @Override
  public Locale[] getAvailableLocales() {
    return m_availableLocales;
  }

  @Override
  public DecimalFormat getCurrencyInstance(Locale locale) {
    Locale defaultLocaleForCountry = getDefaultLocaleForCountry(locale);
    return super.getCurrencyInstance(ObjectUtility.nvl(defaultLocaleForCountry, locale));
  }

  @Override
  public DecimalFormat getIntegerInstance(Locale locale) {
    Locale defaultLocaleForCountry = getDefaultLocaleForCountry(locale);
    return super.getIntegerInstance(ObjectUtility.nvl(defaultLocaleForCountry, locale));
  }

  @Override
  public DecimalFormat getNumberInstance(Locale locale) {
    Locale defaultLocaleForCountry = getDefaultLocaleForCountry(locale);
    return super.getNumberInstance(ObjectUtility.nvl(defaultLocaleForCountry, locale));
  }

  @Override
  public DecimalFormat getPercentInstance(Locale locale) {
    Locale defaultLocaleForCountry = getDefaultLocaleForCountry(locale);
    return super.getPercentInstance(ObjectUtility.nvl(defaultLocaleForCountry, locale));
  }

  private void init(Locale locale, String primaryCountryLanguage) {
    m_customLocales.add(locale);
    Locale primaryCountryLocale = new Locale(primaryCountryLanguage, locale.getCountry());
    m_countryDefaultLocaleMap.put(primaryCountryLocale.getCountry(), primaryCountryLocale);
  }

  private Locale getDefaultLocaleForCountry(Locale locale) {
    if (!m_customLocales.contains(locale)) {
      return null;
    }

    Locale countryDefaultLocale = m_countryDefaultLocaleMap.get(locale.getCountry());
    if (countryDefaultLocale == null) {
      LOG.warn("Unexpected: No default locale found for country '{}'", locale.getCountry());
      return null;
    }
    return countryDefaultLocale;
  }
}
