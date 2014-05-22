package org.cotrix.web.manage.client.codelist.event;

import org.cotrix.web.common.shared.codelist.UICodelist;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RemoveCodelistEvent extends GenericEvent {
	
	private UICodelist codelist;

	public RemoveCodelistEvent(UICodelist codelist) {
		this.codelist = codelist;
	}

	public UICodelist getCodelist() {
		return codelist;
	}
}
