package org.cotrix.web.menu.client.view;

import java.util.EnumSet;
import java.util.Set;

import org.cotrix.web.share.client.util.FadeAnimation;

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
	
	protected static final double BASE_OPACITY = 0.2;

	@UiTemplate("MenuView.ui.xml")
	interface MenuViewUiBinder extends UiBinder<Widget, MenuViewImpl> {}
	private static MenuViewUiBinder uiBinder = GWT.create(MenuViewUiBinder.class);

	@UiField Label homeMenu;
	@UiField Label importMenu;
	@UiField Label manageMenu;
	@UiField Label publishMenu;

	@UiField Style style;

	protected Set<Menu> enabledMenu = EnumSet.noneOf(Menu.class);

	interface Style extends CssResource {
		String menuSelected();
		String menuDisabled();
		String menuLink();
	}

	private Presenter presenter;

	public MenuViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		setupMenu();
	}

	protected void setupMenu()
	{
		addListener(homeMenu, Menu.HOME);
		homeMenu.getElement().getStyle().setOpacity(BASE_OPACITY);
		
		addListener(importMenu, Menu.IMPORT);
		importMenu.getElement().getStyle().setOpacity(BASE_OPACITY);
		
		addListener(manageMenu, Menu.MANAGE);
		manageMenu.getElement().getStyle().setOpacity(BASE_OPACITY);
		
		addListener(publishMenu, Menu.PUBLISH);
		publishMenu.getElement().getStyle().setOpacity(BASE_OPACITY);
	}
	
	protected void addListener(final Label item, final Menu menu)
	{
		item.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (!enabledMenu.contains(menu)) return;
				resetMenu();
				item.setStyleName(style.menuSelected(), true);
				presenter.onMenuClicked(menu);
			}
		});
	}
	

	@Override
	public void setSelected(Menu menu) {
		resetMenu();
		Label item = getLabel(menu);
		item.setStyleName(style.menuSelected(), true);
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
		Label menuLabel = getLabel(menu);
		menuLabel.setVisible(visible);
	}
	
	@Override
	public void setEnabled(Menu menu, boolean enabled) {
		Label menuLabel = getLabel(menu);
		menuLabel.setStyleName(style.menuLink(), enabled);
		if (enabled) enabledMenu.add(menu);
		else enabledMenu.remove(menu);
	}

	@Override
	public void makeAvailable(Menu menu) {
		Label menuLabel = getLabel(menu);
		FadeAnimation animation = new FadeAnimation(menuLabel.getElement());
		animation.fadeIn(BASE_OPACITY);
	}
	
	protected Label getLabel(Menu menu) {
		switch (menu) {
			case HOME: return homeMenu;
			case IMPORT: return importMenu;
			case MANAGE: return manageMenu;
			case PUBLISH: return publishMenu;
		}
		throw new IllegalArgumentException("The specified menu "+menu+" don't bind to a widget");
	}

}
