package org.cotrix.web.menu.client.presenter;

import org.cotrix.web.menu.client.view.MenuView;
import org.cotrix.web.menu.client.view.MenuView.Menu;
import org.cotrix.web.share.client.CotrixModule;
import org.cotrix.web.share.client.event.CotrixBus;
import org.cotrix.web.share.client.event.SwitchToModuleEvent;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MenuPresenter implements Presenter<MenuPresenter> , MenuView.Presenter {

	@Inject
	@CotrixBus
	protected EventBus cotrixBus;
	protected MenuView view;
	
	@Inject
	public MenuPresenter(MenuView view) {
		this.view = view;
		this.view.setPresenter(this);
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public void onMenuClicked(Menu menu) {
		CotrixModule module = getModuel(menu);
		cotrixBus.fireEvent(new SwitchToModuleEvent(module));
	}
	
	protected CotrixModule getModuel(Menu menu){
		switch (menu) {
			case HOME: return CotrixModule.HOME;
			case IMPORT: return CotrixModule.IMPORT;
			case MANAGE: return CotrixModule.MANAGE;
			case PUBLISH: return CotrixModule.PUBLISH;
			default: throw new IllegalArgumentException("Unknow menu "+menu);
		}
	}
}
