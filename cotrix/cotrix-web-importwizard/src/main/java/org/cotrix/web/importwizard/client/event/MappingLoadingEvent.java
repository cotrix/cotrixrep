package org.cotrix.web.importwizard.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MappingLoadingEvent extends GwtEvent<MappingLoadingEvent.MappingLoadingHandler> {

	public static Type<MappingLoadingHandler> TYPE = new Type<MappingLoadingHandler>();

	public interface MappingLoadingHandler extends EventHandler {
		void onMappingLoading(MappingLoadingEvent event);
	}

	public MappingLoadingEvent() {
	}

	@Override
	protected void dispatch(MappingLoadingHandler handler) {
		handler.onMappingLoading(this);
	}

	@Override
	public Type<MappingLoadingHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MappingLoadingHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new MappingLoadingEvent());
	}
}
