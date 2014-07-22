package org.cotrix.web.publish.client.resources;

import org.cotrix.web.publish.client.util.DefinitionsMappingPanel.DefinitionMappingsStyle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface Resources extends ClientBundle {
	
	public static final Resources INSTANCE = GWT.create(Resources.class);
	
	@Source("style.css")
	public PublishCss css();
	
	@Source("definitionsMapping.css")
	public DefinitionMappingsStyle definitionsMapping();
	
	public ImageResource arrow();
	public ImageResource arrowDisabled();
	
	public ImageResource csv();

	public ImageResource csvHover();
	
	public ImageResource sdmx();

	public ImageResource sdmxHover();
	
	public ImageResource comet();
	
	public ImageResource cometHover();
	
	public ImageResource attributeIcon();
	public ImageResource attributeIconDisabled();
	
	public ImageResource linkIcon();
	public ImageResource linkIconDisabled();
}