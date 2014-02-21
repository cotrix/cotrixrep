/**
 * 
 */
package org.cotrix.web.codelistmanager.client.resources;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.cellview.client.CellTree;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodelistsResources extends CellTree.Resources {
	
	public static final CodelistsResources INSTANCE = GWT.create(CodelistsResources.class);
	
    /**
     * The styles used in this widget.
     */
    @Source("CodelistsStyle.css")
    CodelistsStyle cellTreeStyle();

	public interface CodelistsStyle extends CellTree.Style {
		
		String versionItem();
		
	}
}
