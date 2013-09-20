package org.cotrix.web.codelistmanager.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class RefreshCodeListsEvent extends
		GwtEvent<RefreshCodeListsEvent.RefreshCodeListsHandler> {

	public static Type<RefreshCodeListsHandler> TYPE = new Type<RefreshCodeListsHandler>();

	public interface RefreshCodeListsHandler extends EventHandler {
		void onRefreshCodeLists(RefreshCodeListsEvent event);
	}

	public RefreshCodeListsEvent() {
	}

	@Override
	protected void dispatch(RefreshCodeListsHandler handler) {
		handler.onRefreshCodeLists(this);
	}

	@Override
	public Type<RefreshCodeListsHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<RefreshCodeListsHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new RefreshCodeListsEvent());
	}
}
