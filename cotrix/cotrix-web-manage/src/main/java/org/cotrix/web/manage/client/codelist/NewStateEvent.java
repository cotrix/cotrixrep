/**
 * 
 */
package org.cotrix.web.manage.client.codelist;

import org.cotrix.web.common.shared.codelist.LifecycleState;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class NewStateEvent extends GenericEvent {
	
	private LifecycleState state;

	public NewStateEvent(LifecycleState state) {
		this.state = state;
	}

	public LifecycleState getState() {
		return state;
	}

}
