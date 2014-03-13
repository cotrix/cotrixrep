package org.cotrix.web.users.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface UsersResources extends ClientBundle {
	
	public static final UsersResources INSTANCE = GWT.create(UsersResources.class);

	ImageResource versionItem();
	
	ImageResource users();
	ImageResource usersGrey();
	ImageResource codelists();
	ImageResource profile();
	ImageResource photo();
	ImageResource listUser();


}