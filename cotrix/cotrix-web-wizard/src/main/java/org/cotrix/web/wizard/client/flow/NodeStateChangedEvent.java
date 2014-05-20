package org.cotrix.web.wizard.client.flow;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class NodeStateChangedEvent extends GwtEvent<NodeStateChangedEvent.NodeStateChangedHandler> {

	public static Type<NodeStateChangedHandler> TYPE = new Type<NodeStateChangedHandler>();

	public interface NodeStateChangedHandler extends EventHandler {
		void onNodeStateChange(NodeStateChangedEvent event);
	}

	public interface HasNodeStateChangedHandlers extends HasHandlers {
		HandlerRegistration addNodeStateChangeHandler(NodeStateChangedHandler handler);
	}

	public NodeStateChangedEvent() {
	}

	@Override
	protected void dispatch(NodeStateChangedHandler handler) {
		handler.onNodeStateChange(this);
	}

	@Override
	public Type<NodeStateChangedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<NodeStateChangedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new NodeStateChangedEvent());
	}
}
