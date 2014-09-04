package org.acme;

import static org.junit.Assert.*;

import org.cotrix.web.common.shared.codelist.attributedefinition.UIRange;
import org.cotrix.web.manage.client.codelist.common.Occurrences;
import org.junit.Test;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class OccurrencesTest {


	@Test
	public void testToRange() {
		UIRange range = Occurrences.CUSTOM.toRange(15, 18);
		assertEquals(15, range.getMin());
		assertEquals(18, range.getMax());
		
		range = Occurrences.AT_MOST.toRange(15, 18);
		assertEquals(0, range.getMin());
		assertEquals(18, range.getMax());
		
		range = Occurrences.ONCE.toRange(15, 18);
		assertEquals(1, range.getMin());
		assertEquals(1, range.getMax());
	}

	@Test
	public void testToOccurrences() {
		UIRange custom = new UIRange(15, 18);
		assertEquals(Occurrences.CUSTOM, Occurrences.toOccurrences(custom));
		
		/*UIRange atleast = new UIRange(16, Integer.MAX_VALUE);
		assertEquals(Occurrences.AT_LEAST, Occurrences.toOccurrences(atleast));*/
		
		UIRange atmost = new UIRange(0, 17);
		assertEquals(Occurrences.AT_MOST, Occurrences.toOccurrences(atmost));

		UIRange once = new UIRange(1, 1);
		assertEquals(Occurrences.ONCE, Occurrences.toOccurrences(once));
		
		UIRange atmostonce = new UIRange(0, 1);
		assertEquals(Occurrences.AT_MOST_ONCE, Occurrences.toOccurrences(atmostonce));
		
		/*UIRange arbitrarily = new UIRange(0, Integer.MAX_VALUE);
		assertEquals(Occurrences.ARBITRARY, Occurrences.toOccurrences(arbitrarily));*/
		
		/*UIRange atleastonce = new UIRange(1, Integer.MAX_VALUE);
		assertEquals(Occurrences.AT_LEAST_ONCE, Occurrences.toOccurrences(atleastonce));*/
	}

}
