package org.cotrix.web.importwizard.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface MyResources extends ClientBundle {
	public static final MyResources INSTANCE = GWT.create(MyResources.class);

	@Source("loading.gif")
	ImageResource loading();


}