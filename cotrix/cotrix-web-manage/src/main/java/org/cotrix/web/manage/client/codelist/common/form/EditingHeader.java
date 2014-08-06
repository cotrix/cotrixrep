/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common.form;

import org.cotrix.web.manage.client.codelist.common.header.ButtonResources;
import org.cotrix.web.manage.client.codelist.common.header.HeaderPanel;
import org.cotrix.web.manage.client.codelist.common.header.HeaderPanel.BandDimension;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class EditingHeader implements ItemPanelHeader {
	
	private HeaderPanel header;
	private boolean switchAvailable;
	
	public EditingHeader(ImageResource icon) {
		header = new HeaderPanel();
		header.setIcon(icon, null);
		switchAvailable = false;
	}
	
	public EditingHeader(ImageResource icon, ButtonResources edit, ButtonResources revert, ButtonResources save) {
		header = new HeaderPanel();
		header.setIcon(icon, null);
		header.setPrimaryButton(edit);
		header.setFirstButton(revert);
		header.setSecondButton(save);
		switchAvailable = false;
	}
	
	public void setSwitch(ButtonResources switchButton) {
		header.setSwitchButton(switchButton);
		switchAvailable = true;
	}
	
	public void setSmall() {
		header.setDimension(BandDimension.SMALL);
	}

	@Override
	public Widget asWidget() {
		return header;
	}

	@Override
	public void setSwitchDown(boolean down) {
		header.setSwitchDown(down);
	}

	@Override
	public void setListener(final HeaderListener listener) {
		header.addPrimaryButtonClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				listener.onButtonClicked(Button.EDIT);
			}
		});
		header.addFirstButtonClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				listener.onButtonClicked(Button.REVERT);
			}
		});
		header.addSecondButtonClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				listener.onButtonClicked(Button.SAVE);
			}
		});
		header.addSwitchButtonClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				listener.onSwitchChange(header.isSwitchDown());
				
			}
		});
	}

	@Override
	public void setHeaderTitle(String title) {
		header.setTitle(title);
	}

	@Override
	public void setHeaderSubtitle(String subtitle) {
		header.setSubtitle(subtitle);
	}

	@Override
	public void setHeaderSelected(boolean selected) {
		header.setSelected(selected);
	}

	@Override
	public void addClickHandler(ClickHandler handler) {
		header.addClickHandler(handler);
	}

	@Override
	public void setSaveVisible(boolean visible) {
		header.setSecondButtonsVisible(visible);
	}

	@Override
	public void setRevertVisible(boolean visible) {
		header.setFirstButtonsVisible(visible);
	}

	@Override
	public void setEditVisible(boolean visible) {
		header.setPrimaryButtonVisible(visible);
	}

	@Override
	public void setControlsVisible(boolean visible) {
		header.setSecondaryButtonsVisible(visible);
	}

	@Override
	public void setSwitchVisible(boolean visible) {
		if (switchAvailable) {
			header.setSwitchVisible(visible);
		}
	}

}
