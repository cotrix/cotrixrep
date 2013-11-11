package org.cotrix.web.publish.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DestinationTypeChangeEvent extends GwtEvent<DestinationTypeChangeEvent.DestinationTypeChangeHandler> {

	public static final DestinationTypeChangeEvent CHANNEL = new DestinationTypeChangeEvent(DestinationType.CHANNEL);
	public static final DestinationTypeChangeEvent FILE = new DestinationTypeChangeEvent(DestinationType.FILE);
	
	public static Type<DestinationTypeChangeHandler> TYPE = new Type<DestinationTypeChangeHandler>();
	
	private DestinationType destinationType;

	public interface DestinationTypeChangeHandler extends EventHandler {
		void onDestinationTypeChange(DestinationTypeChangeEvent event);
	}

	public DestinationTypeChangeEvent(DestinationType destinationType) {
		this.destinationType = destinationType;
	}

	public DestinationType getDestinationType() {
		return destinationType;
	}

	@Override
	protected void dispatch(DestinationTypeChangeHandler handler) {
		handler.onDestinationTypeChange(this);
	}

	@Override
	public Type<DestinationTypeChangeHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<DestinationTypeChangeHandler> getType() {
		return TYPE;
	}
}
