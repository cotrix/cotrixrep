/**
 * 
 */
package org.cotrix.web.manage.client.codelist;

import org.cotrix.web.common.shared.codelist.LifecycleState;
import org.cotrix.web.common.shared.codelist.UICodelist;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistNewStateEvent extends GenericEvent {
	
	private UICodelist codelist;
	private LifecycleState state;

	public CodelistNewStateEvent(UICodelist codelist, LifecycleState state) {
		this.codelist = codelist;
		this.state = state;
	}

	public LifecycleState getState() {
		return state;
	}

	public UICodelist getCodelist() {
		return codelist;
	}

}
