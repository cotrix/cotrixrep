package org.cotrix.web.menu.client.presenter;

import org.cotrix.web.menu.client.view.MenuView;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;

public class MenuPresenter implements Presenter<MenuPresenter> , MenuView.Presenter{

	private final HandlerManager eventBus;
	private final MenuView view;
	private HasWidgets container;
	
	public interface OnCotrixMenuItemClicked{
		void onMenuItemClick(int index);
	}
	private OnCotrixMenuItemClicked onCotrixMenuItemClicked;
	
	public MenuPresenter(HandlerManager eventBus,MenuView view) {
		this.view = view;
		this.eventBus = eventBus;
		this.view.setPresenter(this);
	}
	
	public void go(HasWidgets container) {
		this.container = container;
		this.container.add(view.asWidget());
		view.showMenu();
	}
	public void setOnCotrixMenuItemClicked(OnCotrixMenuItemClicked onCotrixMenuItemClicked){
		this.onCotrixMenuItemClicked = onCotrixMenuItemClicked;
	}
	public void onMenuClicked(int index) {
		onCotrixMenuItemClicked.onMenuItemClick(index);
	}
}
