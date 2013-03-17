package org.cotrix.web.codelistmanager.client.presenter;

import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.client.view.CodeListDetailView;
import org.cotrix.web.codelistmanager.client.view.CodeListView;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class CodeListDetailPresenterImpl implements CodeListDetailPresenter {
	public interface OnNavigationClicked{
		public void onNavigationClicked(boolean isShowingNavLeft);
	}
	private OnNavigationClicked onNavigationClicked;
	
	private ManagerServiceAsync rpcService;
	private HandlerManager eventBus;
	private CodeListDetailView view;

	@Inject
	public CodeListDetailPresenterImpl(ManagerServiceAsync rpcService,HandlerManager eventBus, CodeListDetailView view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		view.init();
	}
	
	public void setOnNavigationLeftClicked(OnNavigationClicked onNavigationClicked){
		this.onNavigationClicked = onNavigationClicked;
	}

	public void onNavLeftClicked(boolean isShowingNavLeft) {
		if(isShowingNavLeft){
			onNavigationClicked.onNavigationClicked(true);
			view.showNavRight();
		}else{
			onNavigationClicked.onNavigationClicked(false);
			view.showNavLeft();
		}
	}

	public void onCodelistNameClicked(boolean isVisible) {
		view.showMetadataPanel(isVisible);
	}

}
