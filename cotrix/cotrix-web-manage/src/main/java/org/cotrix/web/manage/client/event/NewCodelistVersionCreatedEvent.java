package org.cotrix.web.manage.client.event;

import org.cotrix.web.manage.shared.UICodelistInfo;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class NewCodelistVersionCreatedEvent extends GenericEvent {

	private String oldCodelistId;
	private UICodelistInfo newCodelist;

	public NewCodelistVersionCreatedEvent(String oldCodelistId,
			UICodelistInfo newCodelist) {
		this.oldCodelistId = oldCodelistId;
		this.newCodelist = newCodelist;
	}

	public String getOldCodelistId() {
		return oldCodelistId;
	}

	public UICodelistInfo getNewCodelist() {
		return newCodelist;
	}
	
}
