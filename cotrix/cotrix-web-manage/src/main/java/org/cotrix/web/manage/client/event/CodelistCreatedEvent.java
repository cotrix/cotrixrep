package org.cotrix.web.manage.client.event;

import org.cotrix.web.manage.shared.UICodelistInfo;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistCreatedEvent extends GenericEvent {

	private UICodelistInfo codelistInfo;

	public CodelistCreatedEvent(UICodelistInfo codelistInfo) {
		this.codelistInfo = codelistInfo;
	}

	public UICodelistInfo getCodelistInfo() {
		return codelistInfo;
	}
}
