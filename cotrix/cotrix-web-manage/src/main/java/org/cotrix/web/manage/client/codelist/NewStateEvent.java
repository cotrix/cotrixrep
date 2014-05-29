/**
 * 
 */
package org.cotrix.web.manage.client.codelist;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class NewStateEvent extends GenericEvent {
	
	private String state;

	public NewStateEvent(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

}
