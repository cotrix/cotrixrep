package org.cotrix.web.importwizard.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.util.List;

import org.cotrix.web.importwizard.shared.AttributeMapping;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MappingUpdatedEvent extends GwtEvent<MappingUpdatedEvent.MappingUpdatedHandler> {

	public static Type<MappingUpdatedHandler> TYPE = new Type<MappingUpdatedHandler>();
	private List<AttributeMapping> mapping;
	private boolean userEdit;

	public interface MappingUpdatedHandler extends EventHandler {
		void onMappingUpdated(MappingUpdatedEvent event);
	}

	public MappingUpdatedEvent(List<AttributeMapping> mapping, boolean userEdit) {
		this.mapping = mapping;
		this.userEdit = userEdit;
	}

	public List<AttributeMapping> getMapping() {
		return mapping;
	}

	public boolean isUserEdit() {
		return userEdit;
	}

	@Override
	protected void dispatch(MappingUpdatedHandler handler) {
		handler.onMappingUpdated(this);
	}

	@Override
	public Type<MappingUpdatedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MappingUpdatedHandler> getType() {
		return TYPE;
	}

}
