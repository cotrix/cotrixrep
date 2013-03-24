package org.cotrix.web.codelistmanager.client.presenter;

import java.util.ArrayList;

import org.cotrix.web.codelistmanager.client.ManagerService;
import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.client.view.CodeListManagerView;
import org.cotrix.web.share.shared.Codelist;
import org.cotrix.web.share.shared.CotrixImportModel;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
		codeListPresenter.go(view.getContentPanel());
		codeListPresenter.setOnCodelistItemClicked(this);
		codeListDetailPresenter.go(view.getContentPanel());
		codeListDetailPresenter.setOnNavigationLeftClicked(CodeListManagerPresenterImpl.this);
			
	}

	public void onNavigationClicked(boolean isShowingNavLeft) {
		view.showLeftPanel(isShowingNavLeft);
	}

	public void onCodelistItemClicked(int id) {
		codeListDetailPresenter.setData(id);
	}
	
}
