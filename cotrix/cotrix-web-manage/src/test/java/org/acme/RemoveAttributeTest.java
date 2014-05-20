/**
 * 
 */
package org.acme;

import static org.junit.Assert.*;

import org.cotrix.web.manage.client.codelist.attribute.RemoveItemController;
import org.junit.Before;
import org.junit.Test;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RemoveAttributeTest {
	
	protected RemoveItemController controller;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		controller = new RemoveItemController();
	}

	@Test
	public void testAttributeCantBeRemove() {
		//The user can't edit the codelist
		controller.setUserCanEdit(false);
		//The attribute can be removed
		controller.setItemCanBeRemoved(true);
		assertFalse(controller.canRemove());
		
		//The user can't edit the codelist
		controller.setUserCanEdit(false);
		//The attribute can't be removed
		controller.setItemCanBeRemoved(false);
		assertFalse(controller.canRemove());
		
		//The user can edit the codelist
		controller.setUserCanEdit(true);
		//The attribute can't be removed
		controller.setItemCanBeRemoved(false);
		assertFalse(controller.canRemove());
	}
	
	@Test
	public void testAttributeCanBeRemove() {
		//The user can edit the codelist
		controller.setUserCanEdit(true);
		//The attribute can be removed
		controller.setItemCanBeRemoved(true);
		
		assertTrue(controller.canRemove());
	}

}
