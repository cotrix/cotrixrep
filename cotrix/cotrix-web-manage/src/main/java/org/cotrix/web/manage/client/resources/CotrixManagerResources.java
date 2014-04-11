package org.cotrix.web.manage.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public interface CotrixManagerResources extends ClientBundle {
	
	@Deprecated
	public static final CotrixManagerResources INSTANCE = GWT.create(CotrixManagerResources.class);

	@Source("style.css")
	public CotrixManagerStyle css();
	
	@Source("propertyGrid.css")
	public PropertyGridStyle propertyGrid();
	
	public ImageResource table();
	
	public ImageResource tableDisabled();
	
	public ImageResource close();
	public ImageResource closeSmall();
	
	public ImageResource allColumns();
	
	public ImageResource allNormals();
	
	public ImageResource versionItem();
	
	public ImageResource bullet();
	
	public ImageResource lock();
	public ImageResource lockDisabled();
	
	public ImageResource unlock();
	public ImageResource unlockDisabled();
	
	public ImageResource seal();
	public ImageResource sealDisabled();
	
	public ImageResource attributes();
	public ImageResource attributesDisabled();
	
	public ImageResource metadata();
	public ImageResource metadataDisabled();
	
	public ImageResource filter();
	public ImageResource filterDisabled();
	
	public ImageResource linkType();
	public ImageResource linkTypeDisabled();
	
	@Source("thumb_vertical.png")
	public ImageResource thumbVertical();
	
	@Source("thumb_horz.png")
	public ImageResource thumbHorizontal();
	
	public ImageResource newVersion();
	public ImageResource newVersionDisabled();
	
	
	public ImageResource save();
	public ImageResource edit();
	public ImageResource cancel();
	
	interface CotrixManagerStyle extends CssResource {
		String editor();
		String systemProperty();
	}
	
	public interface PropertyGridStyle extends CssResource {
		String emptyTableWidget();

		String table();

		String header();

		String value();
		
		String valueBoxLeft();
		String valueBoxCenter();
		String valueBoxRight();

		String textValue();
	}

}