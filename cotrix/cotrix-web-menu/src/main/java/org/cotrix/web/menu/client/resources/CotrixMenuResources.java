package org.cotrix.web.menu.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface CotrixMenuResources extends ClientBundle {
	public static final CotrixMenuResources INSTANCE = GWT
			.create(CotrixMenuResources.class);

	@Source("logo.png")
	ImageResource logo();

}