/**
 * 
 */
package org.cotrix.web.importwizard.client.flow;

import java.util.List;

import org.cotrix.web.importwizard.client.flow.FlowUpdatedEvent.HasFlowUpdatedHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface FlowNode<T> extends HasFlowUpdatedHandlers {
	
	public T getItem();
	
	public FlowNode<T> getNext();
	
	public List<FlowNode<T>> getChildren();
}
