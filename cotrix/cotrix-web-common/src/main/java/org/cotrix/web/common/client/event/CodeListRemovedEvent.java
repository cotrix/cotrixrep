package org.cotrix.web.common.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListRemovedEvent extends
		GwtEvent<CodeListRemovedEvent.CodeListRemovedHandler> {

	public static Type<CodeListRemovedHandler> TYPE = new Type<CodeListRemovedHandler>();

	public interface CodeListRemovedHandler extends EventHandler {
		void onCodeListRemoved(CodeListRemovedEvent event);
	}
	
	protected String codelistId;

	public CodeListRemovedEvent(String codelistId) {
		this.codelistId = codelistId;
	}

	/**
	 * @return the codelistId
	 */
	public String getCodelistId() {
		return codelistId;
	}

	@Override
	protected void dispatch(CodeListRemovedHandler handler) {
		handler.onCodeListRemoved(this);
	}

	@Override
	public Type<CodeListRemovedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CodeListRemovedHandler> getType() {
		return TYPE;
	}

}
