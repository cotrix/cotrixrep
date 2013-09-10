package org.cotrix.web.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CotrixResources extends ClientBundle {
	
	public static final CotrixResources INSTANCE = GWT.create(CotrixResources.class);

	@Source("home.png")
	ImageResource home();
	
	@Source("block-howto.png")
	ImageResource howto();
	
	@Source("block-communities.png")
	ImageResource communities();
	
	@Source("block-statistic.png")
	ImageResource statistic();
	
	@Source("style.css")
	public CssResource css();


}