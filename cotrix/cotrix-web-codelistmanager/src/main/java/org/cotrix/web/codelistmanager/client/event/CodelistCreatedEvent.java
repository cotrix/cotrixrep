package org.cotrix.web.codelistmanager.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import org.cotrix.web.codelistmanager.shared.CodelistGroup;

import com.google.gwt.event.shared.HasHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistCreatedEvent extends
		GwtEvent<CodelistCreatedEvent.CodelistCreatedHandler> {

	public static Type<CodelistCreatedHandler> TYPE = new Type<CodelistCreatedHandler>();
	private CodelistGroup codelistGroup;

	public interface CodelistCreatedHandler extends EventHandler {
		void onCodelistCreated(CodelistCreatedEvent event);
	}

	public CodelistCreatedEvent(CodelistGroup codelistGroup) {
		this.codelistGroup = codelistGroup;
	}

	public CodelistGroup getCodelistGroup() {
		return codelistGroup;
	}

	@Override
	protected void dispatch(CodelistCreatedHandler handler) {
		handler.onCodelistCreated(this);
	}

	@Override
	public Type<CodelistCreatedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CodelistCreatedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, CodelistGroup codelistGroup) {
		source.fireEvent(new CodelistCreatedEvent(codelistGroup));
	}
}
