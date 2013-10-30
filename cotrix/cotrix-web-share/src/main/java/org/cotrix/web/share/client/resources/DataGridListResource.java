/**
 * 
 */
package org.cotrix.web.share.client.resources;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.DataGrid;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface DataGridListResource extends DataGrid.Resources {

	public static DataGridListResource INSTANCE = GWT.create(DataGridListResource.class);
	
    @Source("previewLoader.gif")
    ImageResource dataGridLoading();
	
	@Source("datagrid-list.css")
	DataGridListStyle dataGridStyle();
	
	
	public interface DataGridListStyle extends DataGrid.Style {
		
	}

}
