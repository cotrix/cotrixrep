package org.cotrix.web.manage.client.codelist.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import org.cotrix.web.common.shared.codelist.UICode;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeSelectedEvent extends
		GwtEvent<CodeSelectedEvent.CodeSelectedHandler> {

	public static Type<CodeSelectedHandler> TYPE = new Type<CodeSelectedHandler>();
	private UICode code;

	public interface CodeSelectedHandler extends EventHandler {
		void onCodeSelected(CodeSelectedEvent event);
	}

	public CodeSelectedEvent(UICode code) {
		this.code = code;
	}

	public UICode getCode() {
		return code;
	}

	@Override
	protected void dispatch(CodeSelectedHandler handler) {
		handler.onCodeSelected(this);
	}

	@Override
	public Type<CodeSelectedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CodeSelectedHandler> getType() {
		return TYPE;
	}

}
