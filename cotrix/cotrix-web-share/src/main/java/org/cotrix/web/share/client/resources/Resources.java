package org.cotrix.web.share.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface Resources extends ClientBundle {
	public static final Resources INSTANCE = GWT.create(Resources.class);
	
	@Source("popup-arrow.png")
	public ImageResource popupArrow();

}