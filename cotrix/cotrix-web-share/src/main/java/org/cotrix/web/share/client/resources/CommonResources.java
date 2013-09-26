package org.cotrix.web.share.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CommonResources extends ClientBundle {
	public static final CommonResources INSTANCE = GWT.create(CommonResources.class);
	
	@Source("style.css")
	public CommonCss css();
	
	@Source("popup-arrow.png")
	public ImageResource popupArrow();
	
	
	@Source("dataLoader.gif")
	public ImageResource dataLoader();

}