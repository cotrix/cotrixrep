package org.acme;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import org.cotrix.web.common.server.util.Ranges;
import org.junit.Test;

import com.google.gwt.view.client.Range;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RangesTest {
	
	static String FIRST_ELEMENT = "1";
	static String LAST_ELEMENT = "5";
	static List<String> aList = Collections.unmodifiableList(Arrays.asList(FIRST_ELEMENT,"2","3","4",LAST_ELEMENT));
	static NavigableSet<String> aSet = new TreeSet<>(aList);
	
	static Range FULL_RANGE = new Range(0, aList.size());
	static Range EMPTY_RANGE = new Range(0, 0);
	static Range FIRST_ELEMENT_RANGE = new Range(0, 1);
	static Range LAST_ELEMENT_RANGE = new Range(aList.size()-1,2);
	static Range MIDDLE_RANGE = new Range(1,aList.size()-2);

	@Test
	public void testSubListListOfTRange() {
		List<String> subList = Ranges.subList(aList, FULL_RANGE);
		assertEquals(FULL_RANGE.getLength(), subList.size());
		assertEquals(aList, subList);
		
		subList = Ranges.subList(aList, EMPTY_RANGE);
		assertTrue(subList.isEmpty());
		
		subList = Ranges.subList(aList, FIRST_ELEMENT_RANGE);
		assertEquals(1, subList.size());
		assertEquals(FIRST_ELEMENT, subList.iterator().next());
		
		subList = Ranges.subList(aList, LAST_ELEMENT_RANGE);
		assertEquals(1, subList.size());
		assertEquals(LAST_ELEMENT, subList.iterator().next());
		
		subList = Ranges.subList(aList, MIDDLE_RANGE);
		assertEquals(MIDDLE_RANGE.getLength(), subList.size());
		assertFalse(subList.contains(FIRST_ELEMENT));
		assertFalse(subList.contains(LAST_ELEMENT));
		
		subList = Ranges.subList(Collections.<String>emptyList(), MIDDLE_RANGE);
		assertTrue(subList.isEmpty());
	}

	@Test
	public void testSubListReverseOrderListOfTRange() {
		List<String> subList = Ranges.subListReverseOrder(aList, FULL_RANGE);
		assertEquals(FULL_RANGE.getLength(), subList.size());
		//assertEquals(aList, subList);
		
		subList = Ranges.subListReverseOrder(aList, EMPTY_RANGE);
		assertTrue(subList.isEmpty());
		
		subList = Ranges.subListReverseOrder(aList, FIRST_ELEMENT_RANGE);
		assertEquals(1, subList.size());
		assertEquals(LAST_ELEMENT, subList.iterator().next());
		
		subList = Ranges.subListReverseOrder(aList, LAST_ELEMENT_RANGE);
		assertEquals(1, subList.size());
		assertEquals(FIRST_ELEMENT, subList.iterator().next());
		
		subList = Ranges.subListReverseOrder(aList, MIDDLE_RANGE);
		assertEquals(MIDDLE_RANGE.getLength(), subList.size());
		assertFalse(subList.contains(FIRST_ELEMENT));
		assertFalse(subList.contains(LAST_ELEMENT));
		
		subList = Ranges.subListReverseOrder(Collections.<String>emptyList(), MIDDLE_RANGE);
		assertTrue(subList.isEmpty());
	}
	
	public static <T> void assetEquals(Set<T> set, List<T> list) {
		assertEquals(set.size(), list.size());
		Iterator<T> setIterator = set.iterator();
		Iterator<T> listIterator = list.iterator();
		while(setIterator.hasNext()) assertEquals(setIterator.next(), listIterator.next());
	}

}
