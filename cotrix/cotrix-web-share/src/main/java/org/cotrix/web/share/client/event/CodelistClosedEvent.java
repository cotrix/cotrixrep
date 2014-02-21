package org.cotrix.web.share.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.String;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistClosedEvent extends
		GwtEvent<CodelistClosedEvent.CodelistClosedHandler> {

	public static Type<CodelistClosedHandler> TYPE = new Type<CodelistClosedHandler>();
	private String codelistid;

	public interface CodelistClosedHandler extends EventHandler {
		void onCodelistClosed(CodelistClosedEvent event);
	}

	public CodelistClosedEvent(String codelistid) {
		this.codelistid = codelistid;
	}

	public String getCodelistid() {
		return codelistid;
	}

	@Override
	protected void dispatch(CodelistClosedHandler handler) {
		handler.onCodelistClosed(this);
	}

	@Override
	public Type<CodelistClosedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CodelistClosedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String codelistid) {
		source.fireEvent(new CodelistClosedEvent(codelistid));
	}
}
