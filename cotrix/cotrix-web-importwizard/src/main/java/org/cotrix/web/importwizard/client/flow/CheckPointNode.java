/**
 * 
 */
package org.cotrix.web.importwizard.client.flow;

import org.cotrix.web.importwizard.client.flow.FlowUpdatedEvent.FlowUpdatedHandler;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CheckPointNode<T> implements FlowNode<T> {
	
	protected FlowNode<T> innerNode;
	protected CheckPointHandler handler;

	/**
	 * @param innerNode
	 * @param handler
	 */
	public CheckPointNode(FlowNode<T> innerNode, CheckPointHandler handler) {
		this.innerNode = innerNode;
		this.handler = handler;
	}

	public boolean check()
	{
		return handler.check();
	}
	

	@Override
	public HandlerRegistration addFlowUpdatedHandler(FlowUpdatedHandler handler) {
		return innerNode.addFlowUpdatedHandler(handler);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		innerNode.fireEvent(event);
	}

	@Override
	public T getItem() {
		return innerNode.getItem();
	}

	@Override
	public FlowNode<T> getNext() {
		return innerNode.getNext();
	}
	
	public interface CheckPointHandler {
		public boolean check();
	}

}
