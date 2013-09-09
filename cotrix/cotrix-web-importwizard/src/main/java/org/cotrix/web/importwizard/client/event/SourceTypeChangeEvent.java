package org.cotrix.web.importwizard.client.event;

import org.cotrix.web.importwizard.client.util.SourceType;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SourceTypeChangeEvent extends GwtEvent<SourceTypeChangeEvent.SourceTypeChangeHandler> {

	public static final SourceTypeChangeEvent CHANNEL = new SourceTypeChangeEvent(SourceType.CHANNEL);
	public static final SourceTypeChangeEvent FILE = new SourceTypeChangeEvent(SourceType.FILE);
	
	public static Type<SourceTypeChangeHandler> TYPE = new Type<SourceTypeChangeHandler>();
	
	private SourceType sourceType;

	public interface SourceTypeChangeHandler extends EventHandler {
		void onSourceTypeChange(SourceTypeChangeEvent event);
	}

	public SourceTypeChangeEvent(SourceType sourceType) {
		this.sourceType = sourceType;
	}

	public SourceType getSourceType() {
		return sourceType;
	}

	@Override
	protected void dispatch(SourceTypeChangeHandler handler) {
		handler.onSourceTypeChange(this);
	}

	@Override
	public Type<SourceTypeChangeHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<SourceTypeChangeHandler> getType() {
		return TYPE;
	}
}
