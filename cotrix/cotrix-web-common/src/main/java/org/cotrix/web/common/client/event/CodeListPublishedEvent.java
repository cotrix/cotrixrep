package org.cotrix.web.common.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListPublishedEvent extends
		GwtEvent<CodeListPublishedEvent.CodeListPublishedHandler> {

	public static Type<CodeListPublishedHandler> TYPE = new Type<CodeListPublishedHandler>();

	public interface CodeListPublishedHandler extends EventHandler {
		void onCodeListPublished(CodeListPublishedEvent event);
	}
	
	protected String codelistId;

	public CodeListPublishedEvent(String codelistId) {
		this.codelistId = codelistId;
	}

	/**
	 * @return the codelistId
	 */
	public String getCodelistId() {
		return codelistId;
	}

	@Override
	protected void dispatch(CodeListPublishedHandler handler) {
		handler.onCodeListPublished(this);
	}

	@Override
	public Type<CodeListPublishedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CodeListPublishedHandler> getType() {
		return TYPE;
	}

}
