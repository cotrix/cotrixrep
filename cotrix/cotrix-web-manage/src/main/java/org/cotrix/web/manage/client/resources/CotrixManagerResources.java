package org.cotrix.web.manage.client.resources;

import org.cotrix.web.manage.client.codelist.codes.marker.style.MarkersResource;
import org.cotrix.web.manage.client.codelist.common.DetailsPanelStyle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public interface CotrixManagerResources extends ClientBundle {
	
	public static final CotrixManagerResources INSTANCE = GWT.create(CotrixManagerResources.class);

	@Source("style.css")
	public CotrixManagerStyle css();
	
	@Source("propertyGrid.css")
	public PropertyGridStyle propertyGrid();
	
	@Source("attributeRow.css")
	public AttributeRowStyle attributeRow();
	
	@Source("detailsPanel.css")
	public DetailsPanelStyle detailsPanelStyle();
	
	public MarkersResource markers();
	
	public ImageResource table();
	
	public ImageResource tableDisabled();
	
	public ImageResource close();
	public ImageResource closeSmall();
	
	public ImageResource allColumns();
	
	public ImageResource allNormals();
	
	public ImageResource versionItem();
	
	public ImageResource bullet();
	
	public ImageResource lock();
	public ImageResource unlock();
	public ImageResource seal();
	public ImageResource unseal();
	
	public ImageResource metadata();
	public ImageResource codes();
	
	public ImageResource splash();
	
	public ImageResource attributesSelected();
	public ImageResource attributesUnselected();
	public ImageResource attributesDisabled();
	
	public ImageResource filter();
	public ImageResource filterDisabled();

	public ImageResource linksSelected();
	public ImageResource linksUnselected();
	public ImageResource linksDisabled();
	
	public ImageResource markersSelected();
	public ImageResource markersUnselected();
	public ImageResource markersDisabled();
	
	public ImageResource logbookSelected();
	public ImageResource logbookUnselected();
	public ImageResource logbookDisabled();
	public ImageResource logbookBullet();
	
	public ImageResource linksTypes();
	
	public ImageResource attributeTypes();
	
	@Source("thumb_vertical.png")
	public ImageResource thumbVertical();
	
	@Source("thumb_horz.png")
	public ImageResource thumbHorizontal();
	
	public ImageResource newVersion();
	public ImageResource newVersionDisabled();
	
	
	public ImageResource save();
	public ImageResource edit();
	public ImageResource cancel();
	
	public ImageResource addButton();
	public ImageResource addButtonHover();
	
	public ImageResource lockBullet();
	public ImageResource pencilBullet();
	public ImageResource stopBullet();
	
	public ImageResource showMenu();
	public ImageResource radioBullet();
	public ImageResource checkBullet();
	public ImageResource codesBullet();
	
	public ImageResource hideMenu();
	
	public ImageResource markersHeader();
	
	@Source("test.json")
	public TextResource testJson();
	
	
	interface CotrixManagerStyle extends CssResource {
		String editor();
		String systemProperty();
		String addButton();
		String addLabel();
		String addLabelCell();
		String headerCode();
		String noItemsBackground();
		String noItemsLabel();
		String systemAttributeDisclosurePanelLabel();
	}
	
	public interface PropertyGridStyle extends CssResource {
		String emptyTableWidget();

		String header();

		String value();
		
		String valueBoxLeft();
		String valueBoxCenter();
		String valueBoxRight();
		
		String argumentLabel();
		String argumentValue();

		String textValue();
	}
	
	public interface AttributeRowStyle extends CssResource {
		String buttonCell();
		String button();
	}

}