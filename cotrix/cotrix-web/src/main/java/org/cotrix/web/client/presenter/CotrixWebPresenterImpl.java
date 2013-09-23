package org.cotrix.web.client.presenter;

import java.util.EnumMap;

import org.cotrix.web.client.view.CotrixWebView;
import org.cotrix.web.menu.client.presenter.MenuPresenter;
import org.cotrix.web.share.client.CotrixModule;
import org.cotrix.web.share.client.CotrixModuleController;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixWebPresenterImpl implements CotrixWebPresenter {
	
	protected int modulesPanelIndex = 0;
	protected EnumMap<CotrixModule, Integer> indexes = new EnumMap<CotrixModule, Integer>(CotrixModule.class);
	private CotrixWebView view;

	@Inject
	public CotrixWebPresenterImpl(CotrixWebView view) {
		this.view = view;
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	@Override
	public void add(CotrixModuleController moduleController) {
		moduleController.go(view.getModulesPanel());
		indexes.put(moduleController.getModule(), modulesPanelIndex++);
	}

	@Override
	public void setMenu(MenuPresenter menu) {
		menu.go(view.getMenuPanel());		
	}

	@Override
	public void showModule(CotrixModule module) {
		int index = indexes.get(module);
		view.showModule(index);
	}

}
