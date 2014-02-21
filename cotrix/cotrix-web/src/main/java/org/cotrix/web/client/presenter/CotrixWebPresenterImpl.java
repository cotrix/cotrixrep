package org.cotrix.web.client.presenter;

import java.util.EnumMap;

import org.cotrix.web.client.view.CotrixWebView;
import org.cotrix.web.common.client.CotrixModule;
import org.cotrix.web.common.client.CotrixModuleController;
import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.common.client.event.SwitchToModuleEvent;
import org.cotrix.web.menu.client.presenter.MenuPresenter;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixWebPresenterImpl implements CotrixWebPresenter {

	protected int modulesPanelIndex = 0;
	protected EnumMap<CotrixModule, Integer> indexes = new EnumMap<CotrixModule, Integer>(CotrixModule.class);
	protected CotrixWebView view;
	protected MenuPresenter menu;
	
	@Inject @CotrixBus
	protected EventBus cotrixBus; 

	@Inject
	public CotrixWebPresenterImpl(CotrixWebView view) {
		this.view = view;
		this.view.setPresenter(this);
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	@Override
	public void add(CotrixModuleController moduleController) {
		moduleController.go(view.getModulesPanel());
		indexes.put(moduleController.getModule(), modulesPanelIndex++);
		menu.makeAvailable(moduleController.getModule());
	}

	@Override
	public void setMenu(MenuPresenter menu) {
		this.menu = menu;
		menu.go(view.getMenuPanel());		
	}

	@Override
	public void setUserBar(UserBarPresenter userBar)
	{
		userBar.go(view.getUserBarPanel());
	}

	@Override
	public void showModule(CotrixModule module) {
		Integer index = indexes.get(module);
		if (index!=null) {
			view.showModule(index);
			menu.selectModule(module);
		} else Log.warn("Missing module "+module+" forgot to add it?");
	}

	@Override
	public void onTitleAreaClick() {
		cotrixBus.fireEvent(new SwitchToModuleEvent(CotrixModule.HOME));
	}

}
