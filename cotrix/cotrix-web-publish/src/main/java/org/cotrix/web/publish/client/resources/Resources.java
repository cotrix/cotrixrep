package org.cotrix.web.publish.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface Resources extends ClientBundle {
	
	public static final Resources INSTANCE = GWT.create(Resources.class);
	
	@Source("style.css")
	public PublishCss css();
	
	public ImageResource arrow();
	
	public ImageResource csv();

	public ImageResource csvHover();
	
	public ImageResource sdmx();

	public ImageResource sdmxHover();
	
	public ImageResource comet();
	
	public ImageResource cometHover();
	
}