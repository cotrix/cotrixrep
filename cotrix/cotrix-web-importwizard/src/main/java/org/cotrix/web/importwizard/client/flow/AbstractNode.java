/**
 * 
 */
package org.cotrix.web.importwizard.client.flow;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class AbstractNode<T> implements FlowNode<T> {
	
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
}
