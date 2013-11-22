package org.cotrix.web.permissionmanager.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface PermissionsResources extends ClientBundle {
	
	public static final PermissionsResources INSTANCE = GWT.create(PermissionsResources.class);

	
	public ImageResource versionItem();
	


}