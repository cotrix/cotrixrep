package org.cotrix.web.publish.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;

public interface CotrixPublishResources extends ClientBundle {
	public static final CotrixPublishResources INSTANCE = GWT.create(CotrixPublishResources.class);

	@Source("navigation_collapse_left.png")
	ImageResource nav_collapse_left();
	
	@Source("navigation_collapse_right.png")
	ImageResource nav_collapse_right();

	@Source("loading.gif")
	ImageResource loading();

	@Source( "style.css")
	public CssResource css();

}