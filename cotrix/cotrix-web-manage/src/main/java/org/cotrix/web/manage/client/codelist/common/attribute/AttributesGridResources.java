/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common.attribute;

import com.google.gwt.user.cellview.client.PatchedDataGrid;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface AttributesGridResources extends PatchedDataGrid.Resources {

	@Source("AttributesGrid.css")
	DataGridStyle dataGridStyle();
	
	public interface DataGridStyle extends PatchedDataGrid.Style {
	}
}

