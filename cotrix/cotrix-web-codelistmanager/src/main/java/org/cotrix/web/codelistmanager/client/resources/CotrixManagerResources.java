package org.cotrix.web.codelistmanager.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CotrixManagerResources extends ClientBundle {
	public static final CotrixManagerResources INSTANCE = GWT.create(CotrixManagerResources.class);

	@Source("navigation_collapse_left.png")
	ImageResource nav_collapse_left();
	
	@Source("navigation_collapse_right.png")
	ImageResource nav_collapse_right();

	@Source("loading.gif")
	ImageResource loading();

	@Source("style.css")
	public CotrixManagerStyle css();
	
	@Source("table.png")
	public ImageResource table();
	
	@Source("table_disabled.png")
	public ImageResource tableDisabled();
	
	@Source("close.png")
	public ImageResource close();
	
	@Source("plus.png")
	public ImageResource plus();
	
	@Source("minus.png")
	public ImageResource minus();
	
	@Source("allColumns.png")
	public ImageResource allColumns();
	
	@Source("allColumns.png")
	public ImageResource allColumnsHover();
	
	@Source("versionItem.png")
	public ImageResource versionItem();
	
	interface CotrixManagerStyle extends CssResource {
		String buttonAllColumns();
	}

}