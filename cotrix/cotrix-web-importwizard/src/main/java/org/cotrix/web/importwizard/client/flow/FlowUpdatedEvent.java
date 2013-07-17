package org.cotrix.web.importwizard.client.flow;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class FlowUpdatedEvent extends
		GwtEvent<FlowUpdatedEvent.FlowUpdatedHandler> {

	public static Type<FlowUpdatedHandler> TYPE = new Type<FlowUpdatedHandler>();

	public interface FlowUpdatedHandler extends EventHandler {
		void onFlowUpdated(FlowUpdatedEvent event);
	}

	public interface HasFlowUpdatedHandlers extends HasHandlers {
		HandlerRegistration addFlowUpdatedHandler(FlowUpdatedHandler handler);
	}

	public FlowUpdatedEvent() {
	}

	@Override
	protected void dispatch(FlowUpdatedHandler handler) {
		handler.onFlowUpdated(this);
	}

	@Override
	public Type<FlowUpdatedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<FlowUpdatedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new FlowUpdatedEvent());
	}
}
