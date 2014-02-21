package org.cotrix.web.importwizard.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RetrieveAssetEvent extends
		GwtEvent<RetrieveAssetEvent.RetrieveAssetHandler> {

	public static Type<RetrieveAssetHandler> TYPE = new Type<RetrieveAssetHandler>();

	public interface RetrieveAssetHandler extends EventHandler {
		void onRetrieveAsset(RetrieveAssetEvent event);
	}

	public RetrieveAssetEvent() {
	}

	@Override
	protected void dispatch(RetrieveAssetHandler handler) {
		handler.onRetrieveAsset(this);
	}

	@Override
	public Type<RetrieveAssetHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<RetrieveAssetHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new RetrieveAssetEvent());
	}
}
