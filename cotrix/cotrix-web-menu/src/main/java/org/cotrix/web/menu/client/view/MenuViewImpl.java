package org.cotrix.web.menu.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MenuViewImpl extends Composite implements MenuView {

	@UiTemplate("MenuView.ui.xml")
	interface MenuViewUiBinder extends UiBinder<Widget, MenuViewImpl> {}
	private static MenuViewUiBinder uiBinder = GWT.create(MenuViewUiBinder.class);

	@UiField Label homeMenu;
	@UiField Label importMenu;
	@UiField Label manageMenu;
	@UiField Label publishMenu;

	@UiField Style style;


	interface Style extends CssResource {
		String menuSelected();
	}

	private Presenter presenter;

	public MenuViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		setupMenu();
	}

	protected void setupMenu()
	{
		addListener(homeMenu, Menu.HOME);
		addListener(importMenu, Menu.IMPORT);
		addListener(manageMenu, Menu.MANAGE);
		addListener(publishMenu, Menu.PUBLISH);
	}
	
	protected void addListener(final Label item, final Menu menu)
	{
		item.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				resetMenu();
				item.setStyleName(style.menuSelected(), true);
				presenter.onMenuClicked(menu);
			}
		});
	}

	protected void resetMenu() {
		homeMenu.setStyleName(style.menuSelected(), false);
		importMenu.setStyleName(style.menuSelected(), false);
		manageMenu.setStyleName(style.menuSelected(), false);
		publishMenu.setStyleName(style.menuSelected(), false);
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setVisible(Menu menu, boolean visible) {
		switch (menu) {
			case HOME: homeMenu.setVisible(visible); break;
			case IMPORT: importMenu.setVisible(visible); break;
			case MANAGE: manageMenu.setVisible(visible); break;
			case PUBLISH: publishMenu.setVisible(visible); break;
		}
	}

}
