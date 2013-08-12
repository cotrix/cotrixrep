/**
 * 
 */
package org.cotrix.web.importwizard.client.flow;

import org.cotrix.web.importwizard.client.flow.NodeStateChangedEvent.NodeStateChangedHandler;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class AbstractNode<T> implements FlowNode<T> {
	
	protected HandlerManager manager = new HandlerManager(this);
	protected T item;

	public AbstractNode(T item) {
		this.item = item;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public T getItem() {
		return item;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public HandlerRegistration addNodeStateChangeHandler(NodeStateChangedHandler handler) {
		return manager.addHandler(NodeStateChangedEvent.TYPE, handler);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void fireEvent(GwtEvent<?> event) {
		manager.fireEvent(event);		
	}
	
}
