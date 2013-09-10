package org.cotrix.web.codelistmanager.client.view;

import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.user.cellview.client.DataGrid;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 * @param <T>
 */
public class CotrixDataGrid<T> extends DataGrid<T> implements ContextMenuHandler {
	public CotrixDataGrid() {
		super();
	}

	public CotrixDataGrid(int pageSize, Resources resources) {
		super(pageSize, resources);
	}

	public void onContextMenu(ContextMenuEvent event) {
		
	}
}
