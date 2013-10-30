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
	
	public ImageResource loader();
	
	@Source("popup-arrow.png")
	public ImageResource popupArrow();
	
	public ImageResource dataLoader();

	public ImageResource manage();

	public ImageResource refresh();

}