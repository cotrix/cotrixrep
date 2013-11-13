package org.cotrix.web.publish.client.event;

import org.cotrix.web.publish.shared.FormatType;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FormatTypeChangeEvent extends GwtEvent<FormatTypeChangeEvent.FormatTypeChangeHandler> {

	public static final FormatTypeChangeEvent SDMX = new FormatTypeChangeEvent(FormatType.SDMX);
	public static final FormatTypeChangeEvent CSV = new FormatTypeChangeEvent(FormatType.CSV);
	
	public static Type<FormatTypeChangeHandler> TYPE = new Type<FormatTypeChangeHandler>();
	
	private FormatType formatType;

	public interface FormatTypeChangeHandler extends EventHandler {
		void onFormatTypeChange(FormatTypeChangeEvent event);
	}

	public FormatTypeChangeEvent(FormatType formatType) {
		this.formatType = formatType;
	}

	public FormatType getFormatType() {
		return formatType;
	}

	@Override
	protected void dispatch(FormatTypeChangeHandler handler) {
		handler.onFormatTypeChange(this);
	}

	@Override
	public Type<FormatTypeChangeHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<FormatTypeChangeHandler> getType() {
		return TYPE;
	}
}
