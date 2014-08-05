package org.cotrix.web.manage.client.codelist.common.form;

import org.cotrix.web.manage.client.util.LabelHeader.Button;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface ItemPanelHeader extends IsWidget {
	
	public interface HeaderListener {
		public void onButtonClicked(Button button);
		public void onSwitchChange(boolean isDown);
	}

	public void setSwitchDown(boolean down);

	public void setListener(HeaderListener listener);

	public void setHeaderLabel(String label);

	public void setHeaderLabelValue(String value);

	public void setHeaderSelected(boolean selected);

	public HandlerRegistration addClickHandler(ClickHandler handler);

	public void setSaveVisible(boolean visible);

	public void setRevertVisible(boolean visible);

	public void setEditVisible(boolean visible);

	public void setControlsVisible(boolean visible);

}