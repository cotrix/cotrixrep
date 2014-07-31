/**
 * 
 */
package org.cotrix.web.manage.client.codelists;

import org.cotrix.web.common.client.widgets.menu.AbstractMenuItem;
import org.cotrix.web.common.client.widgets.menu.ImageMenuItem;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CodelistsMenuImpl extends DecoratedPopupPanel implements CodelistsMenu {

	private static CodelistsMenuUiBinder uiBinder = GWT.create(CodelistsMenuUiBinder.class);

	@UiTemplate("CodelistsMenu.ui.xml")
	interface CodelistsMenuUiBinder extends UiBinder<Widget, CodelistsMenuImpl> {
	}
	
	private Listener listener;

	public CodelistsMenuImpl() {
		super(true, false);
		setWidget(uiBinder.createAndBindUi(this));
		setStyleName(CotrixManagerResources.INSTANCE.menuStyle().menuPopup());
		addCloseHandler(new CloseHandler<PopupPanel>() {
			
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				if (listener!=null) listener.onHide();
			}
		});
	}

	@UiHandler({"ownershipFilterGroup", "includeFilterGroup", /*"sortGroup",*/ "group"})
	public void onOwnershipSelection(SelectionEvent<AbstractMenuItem> event) {
		ImageMenuItem menuItem = (ImageMenuItem)event.getSelectedItem();
		Log.trace("selected "+menuItem.getValue());
		String value = menuItem.getValue().toUpperCase();
		MenuButton button = MenuButton.valueOf(value);
		fireButtonClick(button);
		hide();
	}
	
	private void fireButtonClick(MenuButton button) {
		if (listener!=null) listener.onButtonClicked(button);
	}
	
	@Override
	public void show(UIObject target) {
		showRelativeTo(target);
	}

	@Override
	public void hide() {
		super.hide();
	}

	@Override
	public void setListener(Listener listener) {
		this.listener = listener;
	}

}
