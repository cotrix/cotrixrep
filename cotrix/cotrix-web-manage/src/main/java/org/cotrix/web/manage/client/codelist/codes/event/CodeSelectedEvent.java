package org.cotrix.web.manage.client.codelist.codes.event;

import org.cotrix.web.common.shared.codelist.UICode;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeSelectedEvent extends GenericEvent {

	private UICode code;

	public CodeSelectedEvent(UICode code) {
		this.code = code;
	}

	public UICode getCode() {
		return code;
	}
}
