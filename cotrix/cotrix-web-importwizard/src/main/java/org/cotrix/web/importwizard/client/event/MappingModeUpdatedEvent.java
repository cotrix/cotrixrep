package org.cotrix.web.importwizard.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import org.cotrix.web.importwizard.shared.MappingMode;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MappingModeUpdatedEvent extends
		GwtEvent<MappingModeUpdatedEvent.MappingModeUpdatedHandler> {

	public static Type<MappingModeUpdatedHandler> TYPE = new Type<MappingModeUpdatedHandler>();
	
	private MappingMode mappingMode;

	public interface MappingModeUpdatedHandler extends EventHandler {
		void onMappingModeUpdated(MappingModeUpdatedEvent event);
	}

	public MappingModeUpdatedEvent(MappingMode mappingMode) {
		this.mappingMode = mappingMode;
	}

	public MappingMode getMappingMode() {
		return mappingMode;
	}

	@Override
	protected void dispatch(MappingModeUpdatedHandler handler) {
		handler.onMappingModeUpdated(this);
	}

	@Override
	public Type<MappingModeUpdatedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MappingModeUpdatedHandler> getType() {
		return TYPE;
	}
}
