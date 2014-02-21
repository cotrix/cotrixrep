package org.cotrix.web.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixStartupEvent extends GwtEvent<CotrixStartupEvent.CotrixStartupHandler> {

	public static Type<CotrixStartupHandler> TYPE = new Type<CotrixStartupHandler>();

	public interface CotrixStartupHandler extends EventHandler {
		void onCotrixStartup(CotrixStartupEvent event);
	}

	public CotrixStartupEvent() {
	}

	@Override
	protected void dispatch(CotrixStartupHandler handler) {
		handler.onCotrixStartup(this);
	}

	@Override
	public Type<CotrixStartupHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CotrixStartupHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new CotrixStartupEvent());
	}
}
