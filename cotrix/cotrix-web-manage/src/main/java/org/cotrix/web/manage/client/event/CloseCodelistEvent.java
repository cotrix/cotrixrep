package org.cotrix.web.manage.client.event;

import org.cotrix.web.common.shared.codelist.UICodelist;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CloseCodelistEvent extends GenericEvent {

	private UICodelist codelist;
	
	public CloseCodelistEvent(UICodelist codelist) {
		this.codelist = codelist;
	}

	public UICodelist getCodelist() {
		return codelist;
	}
}
