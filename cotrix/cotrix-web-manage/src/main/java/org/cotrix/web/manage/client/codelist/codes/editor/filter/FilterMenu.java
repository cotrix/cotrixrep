/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.editor.filter;

import org.cotrix.web.common.client.widgets.menu.AbstractMenuItem;
import org.cotrix.web.common.client.widgets.menu.ImageMenuItem;
import org.cotrix.web.common.client.widgets.menu.RadioMenuGroup;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FilterMenu extends DecoratedPopupPanel {

	private static FilterMenuUiBinder uiBinder = GWT.create(FilterMenuUiBinder.class);

	public enum MenuButton {
		ALL,
		HIGHLIGHTED,
		RECENT_CHANGES,
		SINCE_CREATION,
		SINCE;
	}

	public interface Listener {
		public void onButtonClicked(MenuButton button);
		public void onHide();
	}

	@UiTemplate("FilterMenu.ui.xml")
	interface FilterMenuUiBinder extends UiBinder<Widget, FilterMenu> {
	}

	private Listener listener;
	
	@UiField RadioMenuGroup filterGroup;
	@UiField ImageMenuItem showAllItem;
	@UiField ImageMenuItem highlightedItem;
	@UiField ImageMenuItem sinceItem;

	public FilterMenu() {
		super(true, false);
		setWidget(uiBinder.createAndBindUi(this));
		setStyleName(CotrixManagerResources.INSTANCE.menuStyle().menuPopup());
		addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				if (listener!=null && event.isAutoClosed()) listener.onHide();
			}
		});
	}

	@UiHandler({"filterGroup"})
	public void onOwnershipSelection(SelectionEvent<AbstractMenuItem> event) {
		ImageMenuItem menuItem = (ImageMenuItem)event.getSelectedItem();
		Log.trace("selected "+menuItem.getValue());
		MenuButton button = getButton(menuItem);
		fireButtonClick(button);
		if (button!=MenuButton.SINCE) hide();
	}
	
	private MenuButton getButton(ImageMenuItem item) {
		if (item == null) return null;
		String value = item.getValue().toUpperCase();
		MenuButton button = MenuButton.valueOf(value);
		return button;
	}
	
	public MenuButton getSelectedButton() {
		return getButton((ImageMenuItem)filterGroup.getSelectedItem());
	}
	
	public void setSelectedShowAll() {
		filterGroup.selectItem(showAllItem);
	}

	private void fireButtonClick(MenuButton button) {
		if (listener!=null) listener.onButtonClicked(button);
	}

	public void show(UIObject target) {
		showRelativeTo(target);
	}
	
	public void setHighlightItemEnabled(boolean enabled) {
		highlightedItem.setEnabled(enabled);
	}
	
	public UIObject getSinceItem() {
		return sinceItem;
	}
	
	public void setSinceItemLabel(String label) {
		sinceItem.setLabel(label);
	}
	
	public void resetSinceItemLabel() {
		sinceItem.setLabel("SINCE...");
	}

	@Override
	public void hide() {
		super.hide();
	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

}
