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
	
	@Source("style.css")
	public CssResource css();


}