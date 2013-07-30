package org.cotrix.web.importwizard.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.util.List;

import org.cotrix.web.importwizard.shared.AttributeMapping;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MappingsUpdatedEvent extends GwtEvent<MappingsUpdatedEvent.MappingsUpdatedHandler> {

	public static Type<MappingsUpdatedHandler> TYPE = new Type<MappingsUpdatedHandler>();
	private List<AttributeMapping> mappings;
	private boolean userEdit;

	public interface MappingsUpdatedHandler extends EventHandler {
		void onMappingUpdated(MappingsUpdatedEvent event);
	}

	public MappingsUpdatedEvent(List<AttributeMapping> mappings, boolean userEdit) {
		this.mappings = mappings;
		this.userEdit = userEdit;
	}

	public List<AttributeMapping> getMappings() {
		return mappings;
	}

	public boolean isUserEdit() {
		return userEdit;
	}

	@Override
	protected void dispatch(MappingsUpdatedHandler handler) {
		handler.onMappingUpdated(this);
	}

	@Override
	public Type<MappingsUpdatedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MappingsUpdatedHandler> getType() {
		return TYPE;
	}

}
