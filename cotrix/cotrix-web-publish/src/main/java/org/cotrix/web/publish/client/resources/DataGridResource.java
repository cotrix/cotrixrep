package org.cotrix.web.publish.client.resources;

import com.google.gwt.user.cellview.client.DataGrid;

public interface DataGridResource extends DataGrid.Resources {
	@Source({ DataGrid.Style.DEFAULT_CSS, "CustomDataGrid.css" })
	DataGrid.Style dataGridStyle();
};