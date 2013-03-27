package org.cotrix.web.codelistmanager.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;

public interface CotrixManagerResources extends ClientBundle {
	public static final CotrixManagerResources INSTANCE = GWT.create(CotrixManagerResources.class);

	@Source("navigation_collapse_left.png")
	ImageResource nav_collapse_left();
	
	@Source("navigation_collapse_right.png")
	ImageResource nav_collapse_right();

	@Source( "style.css")
	public CssResource css();

}