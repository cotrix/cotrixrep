package org.cotrix.web.importwizard.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CotrixImportResources extends ClientBundle {
	public static final CotrixImportResources INSTANCE = GWT.create(CotrixImportResources.class);

	@Source("loading.gif")
	ImageResource loading();
	
	@Source( "style.css")
	public CssResource css();

}