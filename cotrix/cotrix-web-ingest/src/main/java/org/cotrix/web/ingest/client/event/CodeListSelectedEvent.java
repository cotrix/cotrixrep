package org.cotrix.web.ingest.client.event;

import org.cotrix.web.ingest.shared.AssetInfo;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListSelectedEvent extends GwtEvent<CodeListSelectedEvent.CodeListSelectedHandler> {

	public static Type<CodeListSelectedHandler> TYPE = new Type<CodeListSelectedHandler>();

	public interface CodeListSelectedHandler extends EventHandler {
		void onCodeListSelected(CodeListSelectedEvent event);
	}
	
	protected AssetInfo selectedCodelist;

	public CodeListSelectedEvent(AssetInfo selectedCodelist) {
		this.selectedCodelist = selectedCodelist;
	}

	/**
	 * @return the selectedCodelist
	 */
	public AssetInfo getSelectedCodelist() {
		return selectedCodelist;
	}

	@Override
	protected void dispatch(CodeListSelectedHandler handler) {
		handler.onCodeListSelected(this);
	}

	@Override
	public Type<CodeListSelectedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CodeListSelectedHandler> getType() {
		return TYPE;
	}

}
