/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists.tree;

import org.cotrix.web.permissionmanager.shared.CodelistGroup.CodelistVersion;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistSelectedEvent extends GenericEvent {
	
	protected CodelistVersion codelist;

	/**
	 * @param codelist
	 */
	public CodelistSelectedEvent(CodelistVersion codelist) {
		this.codelist = codelist;
	}

	/**
	 * @return the codelist
	 */
	public CodelistVersion getCodelist() {
		return codelist;
	}

}
