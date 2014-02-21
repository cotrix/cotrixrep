package org.cotrix.web.ingest.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AssetRetrievedEvent extends
		GwtEvent<AssetRetrievedEvent.AssetRetrievedHandler> {

	public static Type<AssetRetrievedHandler> TYPE = new Type<AssetRetrievedHandler>();

	public interface AssetRetrievedHandler extends EventHandler {
		void onAssetRetrieved(AssetRetrievedEvent event);
	}

	public AssetRetrievedEvent() {
	}

	@Override
	protected void dispatch(AssetRetrievedHandler handler) {
		handler.onAssetRetrieved(this);
	}

	@Override
	public Type<AssetRetrievedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AssetRetrievedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new AssetRetrievedEvent());
	}
}
