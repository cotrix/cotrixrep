/**
 * 
 */
package org.cotrix.web.importwizard.client.flow;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SingleNode<T> extends AbstractNode<T> {
	
	protected FlowNode<T> next;
	
	public SingleNode(T item) {
		super(item);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public FlowNode<T> getNext() {
		return next;
	}

	/**
	 * @param next the next to set
	 */
	public void setNext(FlowNode<T> next) {
		this.next = next;
	}
}
