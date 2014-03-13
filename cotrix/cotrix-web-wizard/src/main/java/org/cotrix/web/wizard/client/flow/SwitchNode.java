/**
 * 
 */
package org.cotrix.web.wizard.client.flow;

import java.util.List;

import org.cotrix.web.wizard.client.flow.SwitchSelectionUpdatedEvent.HasSwitchSelectionUpdatedHandlers;
import org.cotrix.web.wizard.client.flow.SwitchSelectionUpdatedEvent.SwitchSelectionUpdatedHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SwitchNode<T> extends AbstractNode<T> implements SwitchSelectionUpdatedHandler {
	
	protected List<FlowNode<T>> children;
	protected NodeSelector<T> selector;
	
	public SwitchNode(T item, List<FlowNode<T>> children, NodeSelector<T> selector) {
		super(item);
		this.children = children;
		this.selector = selector;
		selector.addSwitchSelectionUpdatedHandler(this);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public FlowNode<T> getNext() {
		FlowNode<T> next = selector.selectNode(children);
		return next;
	}
	
	public interface NodeSelector<T> extends HasSwitchSelectionUpdatedHandlers {
		public FlowNode<T> selectNode(List<FlowNode<T>> children);
	}

	@Override
	public void onSwitchSelectionUpdated(SwitchSelectionUpdatedEvent event) {
		NodeStateChangedEvent.fire(this);
	}

	@Override
	public List<FlowNode<T>> getChildren() {
		return children;
	}

}
