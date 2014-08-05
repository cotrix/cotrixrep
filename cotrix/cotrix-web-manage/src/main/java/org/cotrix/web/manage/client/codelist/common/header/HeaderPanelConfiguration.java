package org.cotrix.web.manage.client.codelist.common.header;

import org.cotrix.web.manage.client.codelist.common.header.HeaderPanel.BandDimension;

import com.google.gwt.resources.client.ImageResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class HeaderPanelConfiguration {
	
	private BandDimension dimension;
	private ImageResource icon;
	private ImageResource disabledIcon;
	
	private ButtonConfiguration primaryButton;
	private ButtonConfiguration firstButton;
	private ButtonConfiguration secondButton;

	public HeaderPanelConfiguration(BandDimension dimension, ImageResource icon, ImageResource disabledIcon,
			ButtonConfiguration primaryButton, ButtonConfiguration firstButton,
			ButtonConfiguration secondButton) {
		this.dimension = dimension;
		this.disabledIcon = disabledIcon;
		this.icon = icon;
		this.primaryButton = primaryButton;
		this.firstButton = firstButton;
		this.secondButton = secondButton;
	}

	public BandDimension getDimension() {
		return dimension;
	}

	public ImageResource getIcon() {
		return icon;
	}
	
	public ImageResource getDisabledIcon() {
		return disabledIcon;
	}

	public ButtonConfiguration getPrimaryButton() {
		return primaryButton;
	}

	public ButtonConfiguration getFirstButton() {
		return firstButton;
	}

	public ButtonConfiguration getSecondButton() {
		return secondButton;
	}
	
}