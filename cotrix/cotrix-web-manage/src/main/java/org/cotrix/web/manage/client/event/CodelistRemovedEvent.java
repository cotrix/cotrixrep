package org.cotrix.web.manage.client.event;

import org.cotrix.web.manage.shared.CodelistGroup;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistRemovedEvent extends GenericEvent {

	private CodelistGroup codelistGroup;

	public CodelistRemovedEvent(CodelistGroup codelistGroup) {
		this.codelistGroup = codelistGroup;
	}

	public CodelistGroup getCodelistGroup() {
		return codelistGroup;
	}
}
