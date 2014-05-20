package org.cotrix.web.manage.client.codelist.event;

import org.cotrix.web.common.shared.codelist.UICode;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeUpdatedEvent extends GenericEvent {

	private UICode code;

	public interface CodeUpdatedHandler extends EventHandler {
		void onCodeUpdated(CodeUpdatedEvent event);
	}

	public CodeUpdatedEvent(UICode code) {
		this.code = code;
	}

	public UICode getCode() {
		return code;
	}
}
