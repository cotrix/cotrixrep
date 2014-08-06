package org.cotrix.web.manage.client.codelist.common.header;

import com.google.gwt.resources.client.ImageResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ButtonResources {
	
	private ImageResource upFace;
	private ImageResource hover;
	private ImageResource disabled;
	private ImageResource downFace;
	private String title;
	
	protected ButtonResources() {
	}

	public ImageResource getUpFace() {
		return upFace;
	}

	public void setUpFace(ImageResource upFace) {
		this.upFace = upFace;
	}

	public ImageResource getHover() {
		return hover;
	}

	public void setHover(ImageResource hover) {
		this.hover = hover;
	}

	public ImageResource getDisabled() {
		return disabled;
	}

	public void setDisabled(ImageResource disabled) {
		this.disabled = disabled;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ImageResource getDownFace() {
		return downFace;
	}

	public void setDownFace(ImageResource downFace) {
		this.downFace = downFace;
	}
}