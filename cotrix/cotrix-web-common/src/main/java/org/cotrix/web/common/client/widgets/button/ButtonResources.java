package org.cotrix.web.common.client.widgets.button;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.CustomButton;
import com.google.gwt.user.client.ui.Image;

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
	
	public void apply(CustomButton button) {
		button.getUpFace().setImage(new Image(upFace));
		if (hover!=null)button.getUpHoveringFace().setImage(new Image(hover));
		if (disabled!=null) button.getUpDisabledFace().setImage(new Image(disabled));
		if (downFace!=null) button.getDownFace().setImage(new Image(downFace));
		button.setTitle(title);
	}	
}