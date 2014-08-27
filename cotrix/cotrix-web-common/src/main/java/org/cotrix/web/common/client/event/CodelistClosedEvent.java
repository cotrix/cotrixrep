package org.cotrix.web.common.client.event;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistClosedEvent extends GenericEvent {

	private String codelistid;

	public CodelistClosedEvent(String codelistid) {
		this.codelistid = codelistid;
	}

	public String getCodelistid() {
		return codelistid;
	}
}
