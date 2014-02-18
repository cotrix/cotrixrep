/**
 * 
 */
package org.cotrix.web.share.client.wizard.flow;

import java.util.List;

import org.cotrix.web.share.client.wizard.flow.NodeStateChangedEvent.HasNodeStateChangedHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface FlowNode<T> extends HasNodeStateChangedHandlers {
	
	public T getItem();
	
	public FlowNode<T> getNext();
	
	public List<FlowNode<T>> getChildren();
}
