package org.cotrix.web.manage.client.codelist.common.header;

import com.google.gwt.resources.client.ImageResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ButtonConfiguration {
	
	private ImageResource upFace;
	private ImageResource hover;
	private ImageResource disabled;
	private String title;
	
	
	public ButtonConfiguration(ImageResource upFace, ImageResource hover,
			ImageResource disabled, String title) {
		this.upFace = upFace;
		this.hover = hover;
		this.disabled = disabled;
		this.title = title;
	}

	public ImageResource getUpFace() {
		return upFace;
	}
	
	public ImageResource getHover() {
		return hover;
	}
	
	public ImageResource getDisabled() {
		return disabled;
	}
	
	public String getTitle() {
		return title;
	}	
}