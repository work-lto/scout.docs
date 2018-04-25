/*
 * Copyright (c) BSI Business Systems Integration AG. All rights reserved.
 * http://www.bsiag.com/
 */
package org.eclipse.scout.rt.platform.dataobject;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.dataobject.fixture.EntityFixtureDo;
import org.eclipse.scout.rt.platform.util.Assertions.AssertionException;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.junit.Test;

/**
 * Test cases for {@link DoEntity} and default implemented {@link IDoEntity} methods.
 */
public class DoEntityTest {

  protected static final Date TEST_DATE = DateUtility.parse("2017-11-30 17:29:12.583", IValueFormatConstants.DEFAULT_DATE_PATTERN);

  @Test
  public void testPutGetHasAttribute() {
    DoEntity entity = new DoEntity();
    assertNull(entity.get(null));
    assertNull(entity.get(""));
    assertNull(entity.get("foo"));

    assertFalse(entity.has(""));
    assertFalse(entity.has("foo"));
    assertFalse(entity.has(null));

    DoEntity childEntity = new DoEntity();
    entity.put("foo", childEntity);
    assertEquals(childEntity, entity.get("foo"));
    assertEquals("foo", entity.getNode("foo").getAttributeName());
    assertEquals("foo", entity.allNodes().values().iterator().next().getAttributeName());

    entity.remove("foo");
    assertNull(entity.get("foo"));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testPutGetHasRefAttribute() {
    DoEntity entity = new DoEntity();
    assertNull(entity.get(""));
    assertNull(entity.get("", Object.class));
    assertNull(entity.get("foo"));
    assertNull(entity.get("foo", Object.class));

    entity.put("foo", "bar");
    assertTrue(entity.has("foo"));
    assertTrue(((DoValue<String>) entity.getNode("foo")).exists());
    assertEquals("bar", entity.get("foo", String.class));
    assertEquals("bar", entity.get("foo"));

    assertNotNull(entity.get("foo"));
    assertEquals("bar", ((DoValue<String>) entity.getNode("foo")).get());

    entity.remove("foo");
    assertNull(entity.get("foo"));

    DoValue<String> value = DoValue.of("bar2");
    entity.putNode("foo2", value);
    assertTrue(((DoValue<String>) entity.getNode("foo2")).exists());
    assertEquals("bar2", entity.get("foo2", String.class));
    assertEquals("bar2", entity.get("foo2"));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testPutGetHasListAttribute() {
    DoEntity entity = new DoEntity();
    assertNull(entity.getList("", Object.class));
    assertNull(entity.getList("foo", Object.class));

    entity.putList("foo", Arrays.asList("bar"));
    assertTrue(entity.has("foo"));
    assertTrue(((DoList<String>) entity.getNode("foo")).exists());
    assertEquals("bar", entity.getList("foo", String.class).get(0));
    assertEquals(Arrays.asList("bar"), entity.get("foo"));
    assertEquals(Arrays.asList("bar"), entity.getList("foo"));
  }

  @Test
  public void testAllAttributeNodes() {
    DoEntity entity = new DoEntity();
    assertTrue(entity.allNodes().isEmpty());
    entity.put("attribute", "foo");
    entity.put("attribute2", "bar");
    Set<String> expected = new LinkedHashSet<>();
    expected.add("attribute");
    expected.add("attribute2");
    assertEquals(expected, entity.allNodes().keySet());
  }

  @Test
  public void testAllAttributes() {
    DoEntity entity = new DoEntity();
    assertTrue(entity.all().isEmpty());
    entity.put("attribute", "foo");
    entity.put("attribute2", "bar");
    Set<String> expectedKeys = new LinkedHashSet<>();
    expectedKeys.add("attribute");
    expectedKeys.add("attribute2");
    Set<String> expectedValues = new LinkedHashSet<>();
    expectedValues.add("foo");
    expectedValues.add("bar");
    assertEquals(expectedKeys, entity.all().keySet());
    assertEquals(expectedValues, new HashSet<>(entity.all().values()));
  }

  @Test
  public void testAllAttributesMapper() {
    DoEntity entity = new DoEntity();
    assertTrue(entity.all().isEmpty());
    entity.put("attribute", 100);
    entity.put("attribute2", 200);
    entity.put("attribute1", 300);
    Map<String, Integer> all = entity.all((value) -> (Integer) value);
    assertEquals(new Integer(100), all.get("attribute"));
    assertEquals(new Integer(200), all.get("attribute2"));
    // expect same order of attributes
    assertArrayEquals(new String[]{"attribute", "attribute2", "attribute1"}, all.keySet().toArray());
  }

  @Test
  public void testPutAttribute() {
    DoEntity entity = new DoEntity();
    entity.put("foo", "value1");
    DoNode<?> attribute = entity.getNode("foo");
    assertEquals("value1", entity.get("foo"));
    entity.put("foo", "value2");
    assertEquals("value2", entity.get("foo"));
    assertSame(attribute, entity.getNode("foo"));
  }

  @Test
  public void testPutListAttribute() {
    DoEntity entity = new DoEntity();
    entity.putList("foo", Arrays.asList("value1"));
    DoNode<?> attribute = entity.getNode("foo");
    assertEquals("value1", entity.getList("foo", String.class).get(0));
    entity.putList("foo", Arrays.asList("value2"));
    assertEquals("value2", entity.getList("foo", String.class).get(0));
    assertSame(attribute, entity.getNode("foo"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAllAttributeNodesImmutable() {
    DoEntity entity = new DoEntity();
    entity.allNodes().put("foo", null);
  }

  @Test(expected = AssertionException.class)
  public void testChangeAttributeNodeType() {
    DoEntity entity = new DoEntity();
    entity.putList("foo", Arrays.asList("value1"));
    entity.put("foo", "bar");
  }

  @Test
  public void testUpdateAttributeNode() {
    DoEntity entity = new DoEntity();
    entity.put("foo", "bar");
    assertEquals("bar", entity.get("foo"));
    entity.put("foo", 42);
    assertEquals(42, entity.get("foo"));
  }

  @Test(expected = AssertionException.class)
  public void testValueAttributeNameNull() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.put(null, null);
  }

  @Test(expected = AssertionException.class)
  public void testListAttributeNameNull() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.putList(null, Arrays.asList());
  }

  @Test(expected = AssertionException.class)
  public void testNodeNameNull() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.putNode(null, new DoValue<>());
  }

  @Test(expected = AssertionException.class)
  public void testNodeNull() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.putNode("foo", null);
  }

  @Test
  public void testNodeValueNull() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.put("foo", null);
    assertNull(entity.get("foo"));
    assertNull(entity.getNode("foo").get());
    assertTrue(entity.has("foo"));
  }

  @Test
  public void testNodeListNull() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.putList("foo", null);
    assertNotNull(entity.get("foo")); // DoList with null value is handled as empty list
    assertNotNull(entity.getNode("foo").get());
    assertTrue(((List<?>) entity.get("foo")).isEmpty());
    assertTrue(entity.getList("foo").isEmpty());
    assertTrue(((List<?>) entity.getNode("foo").get()).isEmpty());
    assertTrue(entity.has("foo"));
  }

  @Test
  public void testGetString() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.put("foo", "bar");
    assertEquals("bar", entity.getString("foo"));
  }

  @Test(expected = AssertionException.class)
  public void testGetString_notInstanceOf() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.put("bar", 42);
    entity.getString("bar");
  }

  @Test
  public void testGetString_NullValue() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.put("bar", null);
    assertNull(entity.getString("bar"));
  }

  @Test
  public void testGetList() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.putList("bar", Arrays.asList("foo", 1, true));
    assertEquals(Arrays.asList("foo", 1, true), entity.getList("bar"));
  }

  @Test
  public void testGetList_nullValue() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.putList("bar", null);
    assertTrue(entity.getList("bar").isEmpty());
  }

  @Test
  public void testGetList_nullItemValue() {
    DoEntity entity = BEANS.get(DoEntity.class);
    List<Object> list = newListWithOneNullValue();
    entity.putList("bar", list);
    assertEquals(null, entity.getList("bar").get(0));
  }

  @Test
  public void testGetList_missingNode() {
    DoEntity entity = BEANS.get(DoEntity.class);
    assertNull(entity.getList("bar"));
  }

  @Test
  public void testGetStringList() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.putList("foo", Arrays.asList("bar", "baz"));
    assertEquals("bar", entity.getStringList("foo").get(0));
    assertEquals("baz", entity.getStringList("foo").get(1));
    assertEquals(Arrays.asList("bar", "baz"), entity.getStringList("foo"));
  }

  @Test(expected = AssertionException.class)
  public void testGetStringList_notInstanceOf() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.putList("foo", Arrays.asList(true));
    entity.getStringList("foo");
  }

  @Test
  public void testGetStringList_nullValue() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.putList("bar", null);
    assertTrue(entity.getStringList("bar").isEmpty());
  }

  @Test
  public void testGetStringList_nullItemValue() {
    DoEntity entity = BEANS.get(DoEntity.class);
    List<Object> list = newListWithOneNullValue();
    entity.putList("bar", list);
    assertEquals(null, entity.getStringList("bar").get(0));
  }

  @Test
  public void testGetBoolean() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.put("foo", true);
    assertEquals(true, entity.getBoolean("foo"));
  }

  @Test(expected = AssertionException.class)
  public void testGetBoolean_notInstanceOf() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.put("bar", "false");
    entity.getBoolean("bar");
  }

  @Test
  public void testGetBoolean_nullValue() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.put("bar", null);
    assertNull(entity.getBoolean("bar"));
  }

  @Test
  public void testGetBooleanList() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.putList("foo", Arrays.asList(true, false));
    assertEquals(true, entity.getBooleanList("foo").get(0));
    assertEquals(false, entity.getBooleanList("foo").get(1));
    assertEquals(Arrays.asList(true, false), entity.getBooleanList("foo"));
  }

  @Test(expected = AssertionException.class)
  public void testGetBooleanList_notInstanceOf() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.putList("bar", Arrays.asList("true", 0, false));
    entity.getBooleanList("bar");
  }

  @Test
  public void testGetBooleanList_nullValue() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.putList("bar", null);
    assertTrue(entity.getBooleanList("bar").isEmpty());
  }

  @Test
  public void testGetBooleanList_nullItemValue() {
    DoEntity entity = BEANS.get(DoEntity.class);
    List<Object> list = newListWithOneNullValue();
    entity.putList("bar", list);
    assertEquals(null, entity.getBooleanList("bar").get(0));
  }

  @Test
  public void testGetDecimal() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.put("foo", 42);
    assertEquals(new BigDecimal("42"), entity.getDecimal("foo"));

    entity.put("bar", 42.0);
    assertEquals(new BigDecimal("42.0"), entity.getDecimal("bar"));

    entity.put("bar", new BigInteger("42"));
    assertEquals(new BigDecimal("42"), entity.getDecimal("bar"));

    entity.put("bar", new BigDecimal("42.0"));
    assertEquals(new BigDecimal("42.0"), entity.getDecimal("bar"));
  }

  @Test(expected = AssertionException.class)
  public void testGetDecimal_notInstanceOf() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.put("bar", "false");
    entity.getDecimal("bar");
  }

  @Test
  public void testGetDecimal_nullValue() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.put("bar", null);
    assertNull(entity.getDecimal("bar"));
  }

  @Test
  public void testGetDecimalList() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.putList("foo", Arrays.asList(1, 2));
    assertEquals(new BigDecimal("1"), entity.getDecimalList("foo").get(0));
    assertEquals(new BigDecimal("2"), entity.getDecimalList("foo").get(1));
    assertEquals(Arrays.asList(new BigDecimal("1"), new BigDecimal("2")), entity.getDecimalList("foo"));

    entity.putList("bar", Arrays.asList(100.0, 0));
    assertEquals(Arrays.asList(new BigDecimal("100.0"), new BigDecimal("0")), entity.getDecimalList("bar"));
  }

  @Test(expected = AssertionException.class)
  public void testGetDecimalList_notInstanceOf() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.putList("bar", Arrays.asList("true", 0, false));
    entity.getDecimalList("bar");
  }

  @Test
  public void testGetDecimalList_nullValue() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.putList("bar", null);
    assertTrue(entity.getDecimalList("bar").isEmpty());
  }

  @Test
  public void testGetDecimalList_nullItemValue() {
    DoEntity entity = BEANS.get(DoEntity.class);
    List<Object> list = newListWithOneNullValue();
    entity.putList("bar", list);
    assertEquals(null, entity.getDecimalList("bar").get(0));
  }

  protected List<Object> newListWithOneNullValue() {
    List<Object> list = new ArrayList<>();
    list.add(null);
    return list;
  }

  protected Function<Object, String> mapper = new Function<Object, String>() {
    @Override
    public String apply(Object value) {
      return value.toString();
    }
  };

  @Test
  public void testGetMapper() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.put("foo", "true");
    assertEquals("true", entity.get("foo", value -> value.toString()));
    assertEquals("true", entity.get("foo", mapper));
    assertEquals(true, entity.get("foo", value -> Boolean.valueOf((String) value)));
  }

  @Test
  public void testGetMapper_defaultDateParser() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.put("date", DateUtility.format(TEST_DATE, IValueFormatConstants.DEFAULT_DATE_PATTERN));
    Date date = entity.get("date", IValueFormatConstants.parseDefaultDate);
    assertEquals(TEST_DATE, date);
  }

  @Test
  public void testGetMapperList() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.putList("foo", Arrays.asList(true, false));
    assertEquals(Arrays.asList("true", "false"), entity.getList("foo", value -> value.toString()));
    assertEquals(Arrays.asList("true", "false"), entity.getList("foo", mapper));
    assertEquals(Arrays.asList(true, false), entity.getList("foo", value -> value));
    assertEquals(Arrays.asList(false, true), entity.getList("foo", value -> !((boolean) value)));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testOptNode() {
    DoEntity entity = BEANS.get(DoEntity.class);
    entity.put("string", "foo");
    assertEquals("foo", entity.optNode("string").get().get());
    assertEquals("foo", entity.optNode("string").map(n -> (String) n.get()).orElse("other"));
    assertTrue(entity.optNode("string").isPresent());

    entity.putList("list", Arrays.asList("one", "two"));
    assertEquals(Arrays.asList("one", "two"), entity.optNode("list").get().get());
    assertEquals(Arrays.asList("one", "two"), entity.optNode("list").map(n -> (List<String>) n.get()).orElse(Arrays.asList("other")));
    assertTrue(entity.optNode("list").isPresent());

    assertFalse(entity.optNode("bar").isPresent());
    assertEquals("else-value", entity.optNode("bar").map(n -> (String) n.get()).orElse("else-value"));

    assertFalse(entity.optNode("any value").isPresent());
    assertEquals("else-value", entity.optNode("any value").map(n -> (String) n.get()).orElse("else-value"));
  }

  @Test
  public void testAttributeName() {
    EntityFixtureDo fixture = BEANS.get(EntityFixtureDo.class);
    assertEquals("id", fixture.id().getAttributeName());
    assertEquals("otherEntities", fixture.otherEntities().getAttributeName());

    fixture.put("foo", "bar");
    assertEquals("foo", fixture.getNode("foo").getAttributeName());
    assertEquals("bar", fixture.getNode("foo").get());

    // attribute name gets set (or updated if already set) when DoValue/DoList become part of a DoEntity
    DoValue<String> barAttribute = new DoValue<>();
    barAttribute.set("bar");
    barAttribute.setAttributeName("barAttribute");
    fixture.put("newBarAttribute", barAttribute);
    assertEquals("newBarAttribute", fixture.getNode("newBarAttribute").getAttributeName());
  }
}
