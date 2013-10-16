package org.cotrix.web.codelistmanager.client.codelist.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import org.cotrix.web.codelistmanager.shared.UICode;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RowSelectedEvent extends
		GwtEvent<RowSelectedEvent.RowSelectedHandler> {

	public static Type<RowSelectedHandler> TYPE = new Type<RowSelectedHandler>();
	private UICode row;

	public interface RowSelectedHandler extends EventHandler {
		void onRowSelected(RowSelectedEvent event);
	}

	public RowSelectedEvent(UICode row) {
		this.row = row;
	}

	public UICode getRow() {
		return row;
	}

	@Override
	protected void dispatch(RowSelectedHandler handler) {
		handler.onRowSelected(this);
	}

	@Override
	public Type<RowSelectedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<RowSelectedHandler> getType() {
		return TYPE;
	}

}
