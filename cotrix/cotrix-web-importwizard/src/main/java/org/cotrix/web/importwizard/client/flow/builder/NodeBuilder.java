/**
 * 
 */
package org.cotrix.web.importwizard.client.flow.builder;

import org.cotrix.web.importwizard.client.flow.FlowManager;
import org.cotrix.web.importwizard.client.flow.CheckPointNode.CheckPointHandler;
import org.cotrix.web.importwizard.client.flow.SwitchNode.NodeSelector;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface NodeBuilder<T> {

	<I extends NodeBuilder<T>> I next(I node);
	SingleNodeBuilder<T> next(T item);
	//TODO don't looks the correct way to declare it
	<I extends NodeBuilder<T>> I hasCheckPoint(CheckPointHandler checkPointHandler);
	
	public interface SingleNodeBuilder<T> extends NodeBuilder<T> {
		SingleNodeBuilder<T> next(T item);
		SwitchNodeBuilder<T> hasAlternatives(NodeSelector<T> selector);
	}

	public interface SwitchNodeBuilder<T> extends NodeBuilder<T> {
		SingleNodeBuilder<T> alternative(T item);
		<I extends NodeBuilder<T>> I alternative(I node);
	}
	
	public interface RootNodeBuilder<T> extends SingleNodeBuilder<T> {
		public FlowManager<T> build();
	}

}
