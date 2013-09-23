package org.cotrix.web.codelistmanager.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
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
}
