/**
 * 
 */
package org.cotrix.web.importwizard.client.flow;

import java.util.List;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SwitchNode<T> extends AbstractNode<T> {
	
	protected List<FlowNode<T>> children;
	protected NodeSelector<T> selector;
	
	public SwitchNode(T item, List<FlowNode<T>> children, NodeSelector<T> selector) {
		super(item);
		this.children = children;
		this.selector = selector;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public FlowNode<T> getNext() {
		FlowNode<T> next = selector.selectNode(children);
		return next;
	}
	
	public interface NodeSelector<T> {
		public FlowNode<T> selectNode(List<FlowNode<T>> children);
	}

}
