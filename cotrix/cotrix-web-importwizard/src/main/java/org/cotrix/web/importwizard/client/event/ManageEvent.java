package org.cotrix.web.importwizard.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ManageEvent extends GwtEvent<ManageEvent.ManageHandler> {

	public static Type<ManageHandler> TYPE = new Type<ManageHandler>();

	public interface ManageHandler extends EventHandler {
		void onManage(ManageEvent event);
	}

	public ManageEvent() {
	}

	@Override
	protected void dispatch(ManageHandler handler) {
		handler.onManage(this);
	}

	@Override
	public Type<ManageHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ManageHandler> getType() {
		return TYPE;
	}
}
