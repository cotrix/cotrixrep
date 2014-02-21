package org.cotrix.web.common.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListImportedEvent extends
		GwtEvent<CodeListImportedEvent.CodeListImportedHandler> {

	public static Type<CodeListImportedHandler> TYPE = new Type<CodeListImportedHandler>();

	public interface CodeListImportedHandler extends EventHandler {
		void onCodeListImported(CodeListImportedEvent event);
	}
	
	protected String codelistId;

	public CodeListImportedEvent(String codelistId) {
		this.codelistId = codelistId;
	}

	/**
	 * @return the codelistId
	 */
	public String getCodelistId() {
		return codelistId;
	}

	@Override
	protected void dispatch(CodeListImportedHandler handler) {
		handler.onCodeListImported(this);
	}

	@Override
	public Type<CodeListImportedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CodeListImportedHandler> getType() {
		return TYPE;
	}

}
