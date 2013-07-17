package org.cotrix.web.importwizard.client.flow;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class SwitchSelectionUpdatedEvent extends
		GwtEvent<SwitchSelectionUpdatedEvent.SwitchSelectionUpdatedHandler> {

	public static Type<SwitchSelectionUpdatedHandler> TYPE = new Type<SwitchSelectionUpdatedHandler>();

	public interface SwitchSelectionUpdatedHandler extends EventHandler {
		void onSwitchSelectionUpdated(SwitchSelectionUpdatedEvent event);
	}

	public interface HasSwitchSelectionUpdatedHandlers extends HasHandlers {
		HandlerRegistration addSwitchSelectionUpdatedHandler(
				SwitchSelectionUpdatedHandler handler);
	}

	public SwitchSelectionUpdatedEvent() {
	}

	@Override
	protected void dispatch(SwitchSelectionUpdatedHandler handler) {
		handler.onSwitchSelectionUpdated(this);
	}

	@Override
	public Type<SwitchSelectionUpdatedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<SwitchSelectionUpdatedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new SwitchSelectionUpdatedEvent());
	}
}
