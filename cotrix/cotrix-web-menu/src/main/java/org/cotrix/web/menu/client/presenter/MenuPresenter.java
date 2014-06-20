package org.cotrix.web.menu.client.presenter;

import org.cotrix.web.common.client.CotrixModule;
import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.common.client.event.SwitchToModuleEvent;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureToggler;
import org.cotrix.web.common.shared.feature.ApplicationFeatures;
import org.cotrix.web.menu.client.view.MenuView;
import org.cotrix.web.menu.client.view.MenuView.Menu;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MenuPresenter implements MenuView.Presenter {

	protected static CotrixModule[] DEFAULT_MODULES = {CotrixModule.HOME, CotrixModule.MANAGE};

	@Inject
	@CotrixBus
	protected EventBus cotrixBus;
	protected MenuView view;

	@Inject
	public MenuPresenter(MenuView view) {
		this.view = view;
		this.view.setPresenter(this);
	}

	@Inject
	protected void bindFeatures(FeatureBinder featureBinder)
	{
		featureBinder.bind(new FeatureToggler() {

			@Override
			public void toggleFeature(boolean active) {
				view.setEnabled(Menu.IMPORT, active);
			}
		}, ApplicationFeatures.IMPORT_CODELIST);

		featureBinder.bind(new FeatureToggler() {

			@Override
			public void toggleFeature(boolean active) {
				view.setEnabled(Menu.PUBLISH, active);
			}
		}, ApplicationFeatures.PUBLISH_CODELIST);
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public void onMenuClicked(Menu menu) {
		CotrixModule module = getModule(menu);
		cotrixBus.fireEvent(new SwitchToModuleEvent(module));
	}

	public void selectModule(CotrixModule module)
	{
		Menu menu = getMenu(module);
		if (menu!=null) view.setSelected(menu);
		else view.resetMenu();
	}

	public void makeAvailable(CotrixModule module) {
		Menu menu = getMenu(module);
		if (menu!=null) {
			view.makeAvailable(menu);
			for (CotrixModule defaultModule:DEFAULT_MODULES) if (defaultModule == module) view.setEnabled(menu, true);
		}
	}

	protected CotrixModule getModule(Menu menu){
		switch (menu) {
			case HOME: return CotrixModule.HOME;
			case IMPORT: return CotrixModule.IMPORT;
			case MANAGE: return CotrixModule.MANAGE;
			case PUBLISH: return CotrixModule.PUBLISH;
			default: throw new IllegalArgumentException("Unknow menu "+menu);
		}
	}

	protected Menu getMenu(CotrixModule module){
		switch (module) {
			case HOME: return Menu.HOME;
			case IMPORT: return Menu.IMPORT;
			case MANAGE: return Menu.MANAGE;
			case PUBLISH: return Menu.PUBLISH;
			default: return null;
		}
	}
}
