package org.cotrix.web.importwizard.client.event;

import org.cotrix.web.importwizard.shared.CodeListType;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListTypeUpdatedEvent extends GwtEvent<CodeListTypeUpdatedEvent.CodeListTypeUpdatedHandler> {

	public static Type<CodeListTypeUpdatedHandler> TYPE = new Type<CodeListTypeUpdatedHandler>();

	public interface CodeListTypeUpdatedHandler extends EventHandler {
		void onCodeListTypeUpdated(CodeListTypeUpdatedEvent event);
	}

	protected CodeListType codeListType;
	
	public CodeListTypeUpdatedEvent(CodeListType codeListType) {
		this.codeListType = codeListType;
	}

	/**
	 * @return the codeListType
	 */
	public CodeListType getCodeListType() {
		return codeListType;
	}

	@Override
	protected void dispatch(CodeListTypeUpdatedHandler handler) {
		handler.onCodeListTypeUpdated(this);
	}

	@Override
	public Type<CodeListTypeUpdatedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CodeListTypeUpdatedHandler> getType() {
		return TYPE;
	}
}
