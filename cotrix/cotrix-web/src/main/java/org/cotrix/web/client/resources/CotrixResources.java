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

	ImageResource home();
	
	@Source("block-howto.png")
	ImageResource howto();
	
	@Source("block-communities.png")
	ImageResource communities();
	
	@Source("block-statistic.png")
	ImageResource statistic();
	
	ImageResource logo();
	
	@Source("style.css")
	public CssResource css();
	
	ImageResource user();
	
	ImageResource userDisabled();


}