package org.cotrix.web.importwizard.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface CotrixImportResources extends ClientBundle {
	public static final CotrixImportResources INSTANCE = GWT.create(CotrixImportResources.class);

	@Source("loading.gif")
	ImageResource loading();

}