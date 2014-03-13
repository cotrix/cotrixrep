/**
 * 
 */
package org.cotrix.web.users.client.codelists.tree;

import org.cotrix.web.users.shared.CodelistGroup.CodelistVersion;

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
