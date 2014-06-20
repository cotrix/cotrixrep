package org.cotrix.web.manage.client.codelist.common.attribute;

import org.cotrix.web.common.shared.codelist.HasAttributes;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributesUpdatedEvent extends GenericEvent {

	private HasAttributes attributedItem;

	public interface AttributesUpdatedHandler extends EventHandler {
		void onAttributesUpdated(AttributesUpdatedEvent event);
	}

	public AttributesUpdatedEvent(HasAttributes attributedItem) {
		this.attributedItem = attributedItem;
	}

	public HasAttributes getAttributedItem() {
		return attributedItem;
	}
}
