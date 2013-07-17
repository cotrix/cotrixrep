package org.cotrix.web.importwizard.client.session;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class SourceTypeChangeEvent extends GwtEvent<SourceTypeChangeEvent.SourceTypeChangeHandler> {

	public static Type<SourceTypeChangeHandler> TYPE = new Type<SourceTypeChangeHandler>();
	private SourceType sourceType;

	public interface SourceTypeChangeHandler extends EventHandler {
		void onSourceTypeChange(SourceTypeChangeEvent event);
	}

	public interface HasSourceTypeChangeHandlers extends HasHandlers {
		HandlerRegistration addSourceTypeChangeHandler(SourceTypeChangeHandler handler);
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

	public static void fire(HasHandlers source, SourceType sourceType) {
		source.fireEvent(new SourceTypeChangeEvent(sourceType));
	}
}
