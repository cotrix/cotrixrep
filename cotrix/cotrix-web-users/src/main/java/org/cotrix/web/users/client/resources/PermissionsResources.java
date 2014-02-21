package org.cotrix.web.users.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface PermissionsResources extends ClientBundle {
	
	public static final PermissionsResources INSTANCE = GWT.create(PermissionsResources.class);

	ImageResource versionItem();
	
	ImageResource users();
	ImageResource usersGrey();
	ImageResource codelists();
	ImageResource profile();
	ImageResource photo();
	ImageResource listUser();


}