package org.cotrix.web.codelistmanager.client.presenter;

import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.client.view.CodeListDetailView;
import org.cotrix.web.codelistmanager.client.view.CodeListView;
import org.cotrix.web.share.shared.CotrixImportModel;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
		HorizontalPanel hp = (HorizontalPanel) container;
		hp.add(view.asWidget());
		hp.setCellWidth(hp.getWidget(1), "100%");
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

	public void setData(int id) {
		rpcService.getCodeListModel(id, new AsyncCallback<CotrixImportModel>() {
			public void onSuccess(CotrixImportModel result) {
				view.setData(result);
			}

			public void onFailure(Throwable caught) {
			}
		});
	}

}
