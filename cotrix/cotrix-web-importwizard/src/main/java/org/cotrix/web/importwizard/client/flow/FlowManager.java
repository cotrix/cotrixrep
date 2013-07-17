/**
 * 
 */
package org.cotrix.web.importwizard.client.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FlowManager<T> {
	
	protected FlowNode<T> flowRoot;
	protected FlowNode<T> currentNode;
	protected Stack<FlowNode<T>> stack;
	
	public FlowManager(FlowNode<T> flowRoot)
	{
		this.flowRoot = flowRoot;
		this.currentNode = flowRoot;
		this.stack = new Stack<FlowNode<T>>();
	}
	
	public T getCurrentItem() {
		return currentNode.getItem();
	}
	
	public boolean isFirst() {
		return stack.size()==0;
	}
	
	public boolean isLast() {
		return currentNode.getNext()==null;
	}
	
	public void goNext() {
		if (currentNode.getNext() == null) throw new IllegalStateException("Flow end reached");
		stack.push(currentNode);
		currentNode = currentNode.getNext();
	}
	
	public void goBack() {
		if (stack.isEmpty()) throw new IllegalStateException("Flow start reached");
		currentNode = stack.pop();
	}
	
	public List<T> getCurrentFlow()
	{
		List<T> flow = new ArrayList<T>();
		FlowNode<T> node = flowRoot;
		while(node.getNext()!=null) {
			flow.add(node.getItem());
			node = node.getNext();
		}
		return flow;
	}
}
