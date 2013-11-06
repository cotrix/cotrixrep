package org.cotrix.web.menu.client.presenter;

import org.cotrix.web.menu.client.view.MenuView;
import org.cotrix.web.menu.client.view.MenuView.Menu;
import org.cotrix.web.share.client.CotrixModule;
import org.cotrix.web.share.client.event.CotrixBus;
import org.cotrix.web.share.client.event.SwitchToModuleEvent;
import org.cotrix.web.share.client.feature.FeatureBinder;
import org.cotrix.web.share.client.feature.HasFeature;
import org.cotrix.web.share.shared.feature.ApplicationFeatures;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MenuPresenter implements MenuView.Presenter {

	@Inject
	@CotrixBus
	protected EventBus cotrixBus;
	protected MenuView view;
	
	@Inject
	public MenuPresenter(MenuView view) {
		this.view = view;
		this.view.setPresenter(this);
		bindFeatures();
		setDefaultMenus();
	}
	
	protected void setDefaultMenus()
	{
		view.setEnabled(Menu.HOME, true);
		view.setEnabled(Menu.MANAGE, true);
	}
	
	protected void bindFeatures()
	{
		FeatureBinder.bind(new HasFeature() {
			
			@Override
			public void unsetFeature() {
				view.setEnabled(Menu.IMPORT, false);
			}
			
			@Override
			public void setFeature() {
				view.setEnabled(Menu.IMPORT, true);
			}
		}, ApplicationFeatures.IMPORT_CODELIST);
		
		FeatureBinder.bind(new HasFeature() {
			
			@Override
			public void unsetFeature() {
				view.setEnabled(Menu.PUBLISH, false);
			}
			
			@Override
			public void setFeature() {
				view.setEnabled(Menu.PUBLISH, true);
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
		view.setSelected(getMenu(module));
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
			default: throw new IllegalArgumentException("Unknow module "+module);
		}
	}
}
