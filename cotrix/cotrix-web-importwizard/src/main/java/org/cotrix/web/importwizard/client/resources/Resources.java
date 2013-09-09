package org.cotrix.web.importwizard.client.resources;

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
	public ImportCss css();

	@Source("loader.gif")
	public ImageResource loader();
	
	@Source("computer.png")
	public ImageResource computer();
	
	@Source("computer-hover.png")
	public ImageResource computerHover();

	@Source("cloud.png")
	public ImageResource cloud();
	
	@Source("cloud-hover.png")
	public ImageResource cloudHover();
	
	@Source("browse.png")
	public ImageResource browse();
	
	@Source("browse-hover.png")
	public ImageResource browseHover();

	@Source("close.png")
	public ImageResource close();
	
	@Source("refresh.png")
	public ImageResource refresh();
	
	@Source("trash.png")
	public ImageResource trash();
	
	@Source("trash_tick.png")
	public ImageResource trashTick();
	
	@Source("download.png")
	public ImageResource download();
	
	@Source("back.png")
	public ImageResource back();
	
	@Source("reload.png")
	public ImageResource reload();
	
	@Source("popup-arrow.png")
	public ImageResource popupArrow();
	
	@Source("manage.png")
	public ImageResource manage();
	
	@Source("import.png")
	public ImageResource importIcon();
	
	
}