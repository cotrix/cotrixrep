package org.cotrix.web.codelistmanager.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import org.cotrix.web.codelistmanager.shared.UICodeListRow;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RowSelectedEvent extends
		GwtEvent<RowSelectedEvent.RowSelectedHandler> {

	public static Type<RowSelectedHandler> TYPE = new Type<RowSelectedHandler>();
	private UICodeListRow row;

	public interface RowSelectedHandler extends EventHandler {
		void onRowSelected(RowSelectedEvent event);
	}

	public RowSelectedEvent(UICodeListRow row) {
		this.row = row;
	}

	public UICodeListRow getRow() {
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
