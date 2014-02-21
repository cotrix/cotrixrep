package org.cotrix.web.common.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.String;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistOpenedEvent extends
		GwtEvent<CodelistOpenedEvent.CodelistOpenedHandler> {

	public static Type<CodelistOpenedHandler> TYPE = new Type<CodelistOpenedHandler>();
	private String codelistId;

	public interface CodelistOpenedHandler extends EventHandler {
		void onCodelistOpened(CodelistOpenedEvent event);
	}

	public CodelistOpenedEvent(String codelistId) {
		this.codelistId = codelistId;
	}

	public String getCodelistId() {
		return codelistId;
	}

	@Override
	protected void dispatch(CodelistOpenedHandler handler) {
		handler.onCodelistOpened(this);
	}

	@Override
	public Type<CodelistOpenedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CodelistOpenedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String codelistId) {
		source.fireEvent(new CodelistOpenedEvent(codelistId));
	}
}
