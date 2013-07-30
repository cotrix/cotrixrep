/**
 * 
 */
package org.cotrix.web.importwizard.client.flow;

import java.util.Collections;
import java.util.List;


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

	@Override
	public List<FlowNode<T>> getChildren() {
		return Collections.singletonList(next);
	}

}
