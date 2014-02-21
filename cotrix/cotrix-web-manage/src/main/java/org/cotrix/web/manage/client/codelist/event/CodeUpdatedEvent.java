package org.cotrix.web.manage.client.codelist.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import org.cotrix.web.common.shared.codelist.UICode;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeUpdatedEvent extends
		GwtEvent<CodeUpdatedEvent.CodeUpdatedHandler> {

	public static Type<CodeUpdatedHandler> TYPE = new Type<CodeUpdatedHandler>();
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

	@Override
	protected void dispatch(CodeUpdatedHandler handler) {
		handler.onCodeUpdated(this);
	}

	@Override
	public Type<CodeUpdatedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CodeUpdatedHandler> getType() {
		return TYPE;
	}

}
