/**
 * 
 */
package org.cotrix.web.importwizard.client.flow;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface FlowNode<T> {
	
	public T getItem();
	
	public FlowNode<T> getNext();
}
