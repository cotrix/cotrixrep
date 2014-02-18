/**
 * 
 */
package org.cotrix.web.share.client.wizard.flow;

import org.cotrix.web.share.client.wizard.flow.SwitchNode.NodeSelector;
import org.cotrix.web.share.client.wizard.flow.SwitchSelectionUpdatedEvent.SwitchSelectionUpdatedHandler;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class AbstractNodeSelector<T> implements NodeSelector<T> {

	protected HandlerManager handlerManager = new HandlerManager(this);
	
	
	public void switchUpdated()
	{
		SwitchSelectionUpdatedEvent.fire(this);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public HandlerRegistration addSwitchSelectionUpdatedHandler(SwitchSelectionUpdatedHandler handler) {
		return handlerManager.addHandler(SwitchSelectionUpdatedEvent.TYPE, handler);
	}
	
	

}
