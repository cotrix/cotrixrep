/**
 * 
 */
package org.cotrix.web.importwizard.client.flow.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cotrix.web.importwizard.client.flow.AbstractNode;
import org.cotrix.web.importwizard.client.flow.FlowManager;
import org.cotrix.web.importwizard.client.flow.FlowNode;
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
	
	protected AbstractNode<T> internalBuild()
	{
		if (built!=null) return built;
		
		if (nexts == null) return new SingleNode<T>(item);
		
		if (nexts.size()==1) {
			AbstractNode<T> next = nexts.get(0).internalBuild();
			
			SingleNode<T> node = new SingleNode<T>(item);
			node.setNext(next);
			
			return node;
		}
		
		if (nexts.size()>1) {
			List<FlowNode<T>> children = new ArrayList<FlowNode<T>>(nexts.size());
			for (FlowManagerBuilder<T> builder:nexts) {
				AbstractNode<T> child = builder.internalBuild();
				children.add(child);
			}
			
			SwitchNode<T> node = new SwitchNode<T>(item, children, selector);
			return node;
		}
		
		return null;
	}
	
	@Override
	public FlowManager<T> build() {
		resetBuilt();
		FlowNode<T> root = internalBuild();
		return new FlowManager<T>(root);
	}

}
