package org.cotrix.web.importwizard.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MappingLoadFailedEvent extends	GwtEvent<MappingLoadFailedEvent.MappingLoadFailedHandler> {

	public static Type<MappingLoadFailedHandler> TYPE = new Type<MappingLoadFailedHandler>();


	public interface MappingLoadFailedHandler extends EventHandler {
		void onMappingLoadFailed(MappingLoadFailedEvent event);
	}

	private Throwable throwable;
	
	public MappingLoadFailedEvent(Throwable throwable) {
		this.throwable = throwable;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	@Override
	protected void dispatch(MappingLoadFailedHandler handler) {
		handler.onMappingLoadFailed(this);
	}

	@Override
	public Type<MappingLoadFailedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MappingLoadFailedHandler> getType() {
		return TYPE;
	}
}
