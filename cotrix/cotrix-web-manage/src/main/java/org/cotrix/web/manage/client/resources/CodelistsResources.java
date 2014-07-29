/**
 * 
 */
package org.cotrix.web.manage.client.resources;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public interface CodelistsResources extends CellTree.Resources {
	
	public static final CodelistsResources INSTANCE = GWT.create(CodelistsResources.class);
	
    @Source("codesBullet.png")
    ImageResource cellTreeClosedItem();

    @Source("codesBullet.png")
    ImageResource cellTreeOpenItem();
	
    /**
     * The styles used in this widget.
     */
    @Source("CodelistsStyle.css")
    CodelistsStyle cellTreeStyle();

	public interface CodelistsStyle extends CellTree.Style {
		
		String versionItem();
		
	}
}
