package org.cotrix.web.manage.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RefreshCodelistsEvent extends
		GwtEvent<RefreshCodelistsEvent.RefreshCodeListsHandler> {

	public static Type<RefreshCodeListsHandler> TYPE = new Type<RefreshCodeListsHandler>();

	public interface RefreshCodeListsHandler extends EventHandler {
		void onRefreshCodeLists(RefreshCodelistsEvent event);
	}

	public RefreshCodelistsEvent() {
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
}
