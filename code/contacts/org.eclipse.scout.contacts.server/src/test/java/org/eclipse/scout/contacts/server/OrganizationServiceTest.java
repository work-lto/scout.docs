/*
 * Copyright (c) 2010, 2023 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.scout.contacts.server;

import static org.junit.Assert.*;

import org.eclipse.scout.contacts.server.organization.OrganizationService;
import org.eclipse.scout.contacts.server.sql.DatabaseSetupService;
import org.eclipse.scout.contacts.server.sql.DerbySqlService;
import org.eclipse.scout.contacts.shared.organization.OrganizationFormData;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link OrganizationService}
 */
@RunWith(ServerTestRunner.class)
@RunWithServerSession(ServerSession.class)
@RunWithSubject("default")
public class OrganizationServiceTest {

  private OrganizationFormData testOrg = new OrganizationFormData();

  @BeforeClass
  public static void setupDatabase() {
    BEANS.get(DerbySqlService.class).createDB();
    BEANS.get(DatabaseSetupService.class).createOrganizationTable();
  }

  @AfterClass
  public static void destroyDBConnections() {
    BEANS.get(DerbySqlService.class).destroySqlConnectionPool();
  }

  @Before
  public void before() {
    testOrg.getName().setValue("test");
    testOrg.getEmail().setValue("testEmail");
    testOrg.getPhone().setValue("000");
  }

  @Test
  public void testCreate() {
    OrganizationService svc = new OrganizationService();
    OrganizationFormData created = svc.create(testOrg);
    assertTestOrg(created);
    assertNotNull(created.getOrganizationId());
  }

  @Test
  public void testLoad() {
    OrganizationService svc = new OrganizationService();
    testOrg.setOrganizationId("testId");
    svc.create(testOrg);
    OrganizationFormData res = svc.load(testOrg);
    assertTestOrg(res);
    assertEquals("testId", res.getOrganizationId());
  }

  @Test
  public void testStore() {
    OrganizationService svc = new OrganizationService();
    testOrg.setOrganizationId("testId2");
    OrganizationFormData created = svc.create(testOrg);
    created.getName().setValue("newName");
    OrganizationFormData res = svc.store(testOrg);
    assertEquals("testId2", res.getOrganizationId());
    assertEquals("newName", res.getName().getValue());
  }

  private void assertTestOrg(OrganizationFormData inputOrg) {
    assertNotNull(inputOrg);
    assertEquals("test", inputOrg.getName().getValue());
    assertEquals("testEmail", inputOrg.getEmail().getValue());
    assertEquals("000", inputOrg.getPhone().getValue());
  }
}
