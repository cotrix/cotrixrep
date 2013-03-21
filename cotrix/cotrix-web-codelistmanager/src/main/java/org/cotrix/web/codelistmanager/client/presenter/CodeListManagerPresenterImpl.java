package org.cotrix.web.codelistmanager.client.presenter;

import org.cotrix.web.codelistmanager.client.ManagerService;
import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.client.view.CodeListManagerView;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class CodeListManagerPresenterImpl implements CodeListManagerPresenter{
	private ManagerServiceAsync rpcService;
	private HandlerManager eventBus;
	private CodeListManagerView view;
	private CodeListPresenter codeListPresenter;
	private CodeListDetailPresenter codeListDetailPresenter;
	
	@Inject
	public CodeListManagerPresenterImpl(ManagerServiceAsync rpcService,HandlerManager eventBus,CodeListPresenter codeListPresenter,CodeListManagerView view,CodeListDetailPresenter codeListDetailPresenter){
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.codeListPresenter = codeListPresenter;
		this.codeListDetailPresenter = codeListDetailPresenter;
		this.view = view;
		this.view.setPresenter(this);
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
		view.init();
		codeListPresenter.go(view.getLeftPanel());
		codeListDetailPresenter.go(view.getRightPanel());
		codeListDetailPresenter.setOnNavigationLeftClicked(this);
	}

	public void onNavigationClicked(boolean isShowingNavLeft) {
		view.showLeftPanel(isShowingNavLeft);
		view.expandRightPanel(isShowingNavLeft);
	}
	
}
