/**
 * 
 */
package org.cotrix.web.share.client.wizard.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import org.cotrix.web.share.client.wizard.flow.FlowUpdatedEvent.FlowUpdatedHandler;
import org.cotrix.web.share.client.wizard.flow.FlowUpdatedEvent.HasFlowUpdatedHandlers;
import org.cotrix.web.share.client.wizard.flow.NodeStateChangedEvent.NodeStateChangedHandler;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FlowManager<T> implements NodeStateChangedHandler, HasFlowUpdatedHandlers {

	protected FlowNode<T> flowRoot;
	protected FlowNode<T> currentNode;
	protected Stack<FlowNode<T>> stack;
	protected HandlerManager handlerManager;

	public FlowManager()
	{
		this.stack = new Stack<FlowNode<T>>();
		handlerManager = new HandlerManager(this);
	}

	/**
	 * @param flowRoot the flowRoot to set
	 */
	public void setFlowRoot(FlowNode<T> flowRoot) {
		this.flowRoot = flowRoot;
		this.currentNode = flowRoot;
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

		if (currentNode instanceof CheckPointNode) {
			boolean checked = ((CheckPointNode<T>)currentNode).check();
			if (!checked) return;
		}

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
		while(node!=null) {
			flow.add(node.getItem());
			node = node.getNext();
		}
		return flow;
	}

	public void reset()
	{
		stack.clear();
		currentNode = flowRoot;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	@Override
	public HandlerRegistration addFlowUpdatedHandler(FlowUpdatedHandler handler) {
		return handlerManager.addHandler(FlowUpdatedEvent.TYPE, handler);
	}
	public String toDot(LabelProvider<T> labelProvider)
	{
		StringBuilder dot = new StringBuilder();
		dot.append("digraph flow {\n");
		visit(dot, flowRoot, labelProvider, new TreeSet<String>());
		dot.append("}\n");
		return dot.toString();
	}
	
	protected void visit(StringBuilder dot, FlowNode<T> node, LabelProvider<T> labelProvider, Set<String> visited)
	{
		//TODO find a better way...
		if (node instanceof CheckPointNode) {
			visit(dot, node.getChildren().get(0), labelProvider, visited);
			return;
		}
		
		String parentLabel = labelProvider.getLabel(node.getItem());
		boolean unsaw = visited.add(parentLabel);
		if (!unsaw) return;
		
		for (FlowNode<T> child:node.getChildren()) {
			if (child == null) continue;
			dot.append('"').append(parentLabel).append('"').append(" -> ").append('"').append(labelProvider.getLabel(child.getItem())).append('"').append('\n');
			visit(dot, child, labelProvider, visited);
		}
	}
	
	public interface LabelProvider<T> {
		public String getLabel(T item);
	}

	@Override
	public void onNodeStateChange(NodeStateChangedEvent event) {
		Log.trace("node state changed firing flow updated");
		FlowUpdatedEvent.fire(this);
	}

}
