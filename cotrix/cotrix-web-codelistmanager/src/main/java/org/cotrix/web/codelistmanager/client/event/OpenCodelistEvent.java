package org.cotrix.web.codelistmanager.client.event;

import org.cotrix.web.share.shared.codelist.UICodelist;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class OpenCodelistEvent extends GwtEvent<OpenCodelistEvent.OpenCodeListHandler> {

	public static Type<OpenCodeListHandler> TYPE = new Type<OpenCodeListHandler>();

	public interface OpenCodeListHandler extends EventHandler {
		void onOpenCodeList(OpenCodelistEvent event);
	}

	protected UICodelist codelist;
	
	public OpenCodelistEvent(UICodelist codelist) {
		this.codelist = codelist;
	}

	/**
	 * @return the codelist
	 */
	public UICodelist getCodelist() {
		return codelist;
	}

	@Override
	protected void dispatch(OpenCodeListHandler handler) {
		handler.onOpenCodeList(this);
	}

	@Override
	public Type<OpenCodeListHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<OpenCodeListHandler> getType() {
		return TYPE;
	}

}
