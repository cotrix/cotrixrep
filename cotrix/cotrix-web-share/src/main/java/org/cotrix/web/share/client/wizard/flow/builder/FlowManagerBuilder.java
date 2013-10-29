/**
 * 
 */
package org.cotrix.web.share.client.wizard.flow.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cotrix.web.share.client.wizard.flow.AbstractNode;
import org.cotrix.web.share.client.wizard.flow.CheckPointNode;
import org.cotrix.web.share.client.wizard.flow.FlowManager;
import org.cotrix.web.share.client.wizard.flow.FlowNode;
import org.cotrix.web.share.client.wizard.flow.SingleNode;
import org.cotrix.web.share.client.wizard.flow.SwitchNode;
import org.cotrix.web.share.client.wizard.flow.CheckPointNode.CheckPointHandler;
import org.cotrix.web.share.client.wizard.flow.NodeStateChangedEvent.NodeStateChangedHandler;
import org.cotrix.web.share.client.wizard.flow.SwitchNode.NodeSelector;
import org.cotrix.web.share.client.wizard.flow.builder.NodeBuilder.*;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FlowManagerBuilder<T> implements SingleNodeBuilder<T>, SwitchNodeBuilder<T>, RootNodeBuilder<T> {
	
	public static <T> RootNodeBuilder<T> startFlow(T item)
	{
		return new FlowManagerBuilder<T>(item, Collections.<FlowManagerBuilder<T>>emptyList());
	}
	
	protected T item;
	protected List<FlowManagerBuilder<T>> nexts;
	protected NodeSelector<T> selector;
	protected CheckPointHandler checkPointHandler;
	
	protected AbstractNode<T> built;
	
	/**
	 * @param item
	 * @param nexts
	 */
	public FlowManagerBuilder(T item, List<FlowManagerBuilder<T>> nexts) {
		this.item = item;
		this.nexts = nexts;
	}

	protected FlowManagerBuilder(T item)
	{
		this.item = item;
	}
	
	public <I extends NodeBuilder<T>> I next(I node) {
		FlowManagerBuilder<T> builder = (FlowManagerBuilder<T>)node;
		nexts = Collections.singletonList(builder);
		return node;
	}
	
	@SuppressWarnings("unchecked")
	public <I extends NodeBuilder<T>> I hasCheckPoint(CheckPointHandler checkPointHandler)
	{
		this.checkPointHandler = checkPointHandler;
		return (I) this;
	}
	
	@Override
	public SingleNodeBuilder<T> next(T item) {
		FlowManagerBuilder<T> builder = new FlowManagerBuilder<T>(item);
		nexts = Collections.singletonList(builder);
		return builder;
	}
	
	@Override
	public SwitchNodeBuilder<T> hasAlternatives(NodeSelector<T> selector) {
		this.selector = selector;
		this.nexts = nexts==null?new ArrayList<FlowManagerBuilder<T>>():new ArrayList<FlowManagerBuilder<T>>(nexts);
		return this;
	}
	
	@Override
	public SingleNodeBuilder<T> alternative(T item) {
		FlowManagerBuilder<T> builder = new FlowManagerBuilder<T>(item);
		this.nexts.add(builder);
		return builder;
	}
	
	@Override
	public <I extends NodeBuilder<T>> I alternative(I node) {
		FlowManagerBuilder<T> builder = (FlowManagerBuilder<T>)node;
		this.nexts.add(builder);
		return node;
	}
	
	protected void resetBuilt()
	{
		built = null;
		if (nexts!=null) for (FlowManagerBuilder<T> next:nexts) next.resetBuilt(); 
	}
	
	protected FlowNode<T> internalBuild(NodeStateChangedHandler handler)
	{
		if (built!=null) return built;
		
		if (nexts == null) {
			SingleNode<T> node = new SingleNode<T>(item);
			node.addNodeStateChangeHandler(handler);
			return wrapCheckPoint(node);
		}
		
		if (nexts.size()==1) {
			FlowNode<T> next = nexts.get(0).internalBuild(handler);
			
			SingleNode<T> node = new SingleNode<T>(item);
			node.setNext(next);
			
			node.addNodeStateChangeHandler(handler);
			
			return wrapCheckPoint(node);
		}
		
		if (nexts.size()>1) {
			List<FlowNode<T>> children = new ArrayList<FlowNode<T>>(nexts.size());
			for (FlowManagerBuilder<T> builder:nexts) {
				FlowNode<T> child = builder.internalBuild(handler);
				children.add(child);
			}
		
			SwitchNode<T> node = new SwitchNode<T>(item, children, selector);
			node.addNodeStateChangeHandler(handler);
			return wrapCheckPoint(node);
		}
		
		return null;
	}
	
	protected FlowNode<T> wrapCheckPoint(FlowNode<T> node)
	{
		if (checkPointHandler == null) return node;
		return new CheckPointNode<T>(node, checkPointHandler);
	}
	
	@Override
	public FlowManager<T> build() {
		resetBuilt();
		FlowManager<T> manager = new FlowManager<T>();
		FlowNode<T> root = internalBuild(manager);
		manager.setFlowRoot(root);
		return manager;
	}

}
