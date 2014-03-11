package org.acme;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.cotrix.web.common.server.util.OrderedLists;
import org.cotrix.web.common.server.util.OrderedLists.ValueProvider;
import org.junit.Before;
import org.junit.Test;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class OrderedListsTest {
	
	protected OrderedLists<Element> orderedLists;

	@Before
	public void setUp() throws Exception {
		orderedLists = new OrderedLists<>();
	}
	
	@Test
	public void testGetSize() {
		assertEquals(0, orderedLists.size());
		
		orderedLists.add(new Element());
		assertEquals(1, orderedLists.size());
	}
	
	@Test
	public void testClear() {
		orderedLists.add(new Element());
		assertEquals(1, orderedLists.size());
		
		orderedLists.clear();
		assertEquals(0, orderedLists.size());
	}

	@Test
	public void testAddField() {
		orderedLists.addField(FIELD1, FIELD_1_PROVIDER);
		
		List<Element> sortedByField1 = orderedLists.getSortedList(FIELD1);;
		assertNotNull(sortedByField1);
		assertTrue(sortedByField1.isEmpty());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMissingGetSortedSet() {
		orderedLists.getSortedList(FIELD1);
	}
	
	@Test
	public void testGetSortedSet() {
		orderedLists.addField(FIELD1, FIELD_1_PROVIDER);
		orderedLists.addField(FIELD2, FIELD_2_PROVIDER);
		orderedLists.addField(FIELD3, FIELD_3_PROVIDER);
		
		Element e1 = el("a","3","B");
		Element e2 = el("b","2","C");
		Element e3 = el("c","1","A");
		
		orderedLists.add(e1);
		orderedLists.add(e2);
		orderedLists.add(e3);
		
		List<Element> orderedByField1 = orderedLists.getSortedList(FIELD1);
		List<Element> orderedByField2 = orderedLists.getSortedList(FIELD2);
		List<Element> orderedByField3 = orderedLists.getSortedList(FIELD3);
		
		assertEquals(3, orderedByField1.size());
		assertEquals(3, orderedByField2.size());
		assertEquals(3, orderedByField3.size());
		
		assertSameOrder(orderedByField1, e1, e2, e3);
		assertSameOrder(orderedByField2, e3, e2, e1);
		assertSameOrder(orderedByField3, e3, e1, e2);
	}
	
	@Test
	public void testGetSortedSetNullField() {
		orderedLists.addField(FIELD1, FIELD_1_PROVIDER);
		
		Element e1 = el(null,"3","B");
		Element e2 = el("b","2","C");
		Element e3 = el("c","1","A");
		
		orderedLists.add(e1);
		orderedLists.add(e2);
		orderedLists.add(e3);
		
		List<Element> orderedByField1 = orderedLists.getSortedList(FIELD1);
		assertEquals(3, orderedByField1.size());
		
		assertSameOrder(orderedByField1, e1, e2, e3);
	}
	
	@Test
	public void testGetSortedSetEqualElements() {
		orderedLists.addField(FIELD1, FIELD_1_PROVIDER);
		
		Element e1 = el("a","3","B");
		
		orderedLists.add(e1);
		orderedLists.add(e1);
		
		List<Element> orderedByField1 = orderedLists.getSortedList(FIELD1);
		assertEquals(2, orderedByField1.size());
		
		assertSameOrder(orderedByField1, e1, e1);
	}
	
	@SafeVarargs
	public static <T> void assertSameOrder(List<T> list, T ... elements) {
		if (list.size()!=elements.length) fail("The two set have a different size "+list.size()+" "+elements.length);
		
		Iterator<T> listIterator = list.iterator();
		for (int i = 0; i < elements.length; i++) {
			T listElement = listIterator.next();
			T element = elements[i];
			assertSame(listElement, element);
		}
	}
	
	public static Element el(String field1, String field2, String field3) {
		Element element = new Element();
		element.field1 = field1;
		element.field2 = field2;
		element.field3 = field3;
		return element;
	}
	
	static String FIELD1 = "field1";
	static String FIELD2 = "field2";
	static String FIELD3 = "field3";
	
	static ValueProvider<Element> FIELD_1_PROVIDER = new ValueProvider<Element>() {

		@Override
		public String getValue(Element item) {
			return item.field1;
		}
	};

	static ValueProvider<Element> FIELD_2_PROVIDER = new ValueProvider<Element>() {

		@Override
		public String getValue(Element item) {
			return item.field2;
		}
	};
	
	static ValueProvider<Element> FIELD_3_PROVIDER = new ValueProvider<Element>() {

		@Override
		public String getValue(Element item) {
			return item.field3;
		}
	};
	
	static class Element {
		String field1;
		String field2;
		String field3;
	}
}
