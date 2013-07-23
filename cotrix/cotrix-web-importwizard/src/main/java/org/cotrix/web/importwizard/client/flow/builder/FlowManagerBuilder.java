/**
 * 
 */
package org.cotrix.web.importwizard.client.flow.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cotrix.web.importwizard.client.flow.AbstractNode;
import org.cotrix.web.importwizard.client.flow.CheckPointNode;
import org.cotrix.web.importwizard.client.flow.FlowManager;
import org.cotrix.web.importwizard.client.flow.FlowNode;
import org.cotrix.web.importwizard.client.flow.CheckPointNode.CheckPointHandler;
import org.cotrix.web.importwizard.client.flow.FlowUpdatedEvent.FlowUpdatedHandler;
import org.cotrix.web.importwizard.client.flow.SingleNode;
import org.cotrix.web.importwizard.client.flow.SwitchNode;
import org.cotrix.web.importwizard.client.flow.SwitchNode.NodeSelector;
import org.cotrix.web.importwizard.client.flow.builder.NodeBuilder.*;

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
		this.nexts = new ArrayList<FlowManagerBuilder<T>>(nexts);
		return this;
	}
	
	@Override
	public SingleNodeBuilder<T> alternative(T item) {
		FlowManagerBuilder<T> builder = new FlowManagerBuilder<T>(item);
		this.nexts.add(builder);
		return builder;
	}
	
	protected void resetBuilt()
	{
		built = null;
		if (nexts!=null) for (FlowManagerBuilder<T> next:nexts) next.resetBuilt(); 
	}
	
	protected FlowNode<T> internalBuild(FlowUpdatedHandler handler)
	{
		if (built!=null) return built;
		
		if (nexts == null) {
			SingleNode<T> node = new SingleNode<T>(item);
			node.addFlowUpdatedHandler(handler);
			return wrapCheckPoint(node);
		}
		
		if (nexts.size()==1) {
			FlowNode<T> next = nexts.get(0).internalBuild(handler);
			
			SingleNode<T> node = new SingleNode<T>(item);
			node.setNext(next);
			
			node.addFlowUpdatedHandler(handler);
			
			return wrapCheckPoint(node);
		}
		
		if (nexts.size()>1) {
			List<FlowNode<T>> children = new ArrayList<FlowNode<T>>(nexts.size());
			for (FlowManagerBuilder<T> builder:nexts) {
				FlowNode<T> child = builder.internalBuild(handler);
				children.add(child);
			}
		
			SwitchNode<T> node = new SwitchNode<T>(item, children, selector);
			node.addFlowUpdatedHandler(handler);
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
