/**
 * 
 */
package org.acme;

import static org.junit.Assert.*;

import org.cotrix.web.manage.client.codelist.attribute.RemoveAttributeController;
import org.junit.Before;
import org.junit.Test;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RemoveAttributeTest {
	
	protected RemoveAttributeController controller;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		controller = new RemoveAttributeController();
	}

	@Test
	public void testAttributeCantBeRemove() {
		//The user can't edit the codelist
		controller.setUserCanEdit(false);
		//The attribute can be removed
		controller.setAttributeCanBeRemoved(true);
		assertFalse(controller.canRemove());
		
		//The user can't edit the codelist
		controller.setUserCanEdit(false);
		//The attribute can't be removed
		controller.setAttributeCanBeRemoved(false);
		assertFalse(controller.canRemove());
		
		//The user can edit the codelist
		controller.setUserCanEdit(true);
		//The attribute can't be removed
		controller.setAttributeCanBeRemoved(false);
		assertFalse(controller.canRemove());
	}
	
	@Test
	public void testAttributeCanBeRemove() {
		//The user can edit the codelist
		controller.setUserCanEdit(true);
		//The attribute can be removed
		controller.setAttributeCanBeRemoved(true);
		
		assertTrue(controller.canRemove());
	}

}
