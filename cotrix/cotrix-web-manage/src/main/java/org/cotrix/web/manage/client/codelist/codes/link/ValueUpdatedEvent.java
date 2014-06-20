package org.cotrix.web.manage.client.codelist.codes.link;

import org.cotrix.web.common.shared.codelist.HasValue;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ValueUpdatedEvent extends GenericEvent {

	private HasValue hasValue;

	public interface AttributesUpdatedHandler extends EventHandler {
		void onAttributesUpdated(ValueUpdatedEvent event);
	}

	public ValueUpdatedEvent(HasValue hasValue) {
		this.hasValue = hasValue;
	}

	public HasValue getHasValue() {
		return hasValue;
	}
}
