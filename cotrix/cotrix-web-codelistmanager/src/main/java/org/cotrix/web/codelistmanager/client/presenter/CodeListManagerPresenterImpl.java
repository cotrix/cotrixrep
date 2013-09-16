package org.cotrix.web.codelistmanager.client.presenter;

import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.client.view.CodeListManagerView;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListManagerPresenterImpl implements CodeListManagerPresenter {
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
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
		
		codeListPresenter.go(view.getWestPanel());
		codeListPresenter.setOnCodelistItemClicked(this);
		codeListDetailPresenter.go(view.getCenterPanel());
		codeListDetailPresenter.setOnNavigationLeftClicked(CodeListManagerPresenterImpl.this);
			
	}

	public void onNavigationClicked(boolean isShowingNavLeft) {
		view.showWestPanel(isShowingNavLeft);
	}

	public void onCodelistItemClicked(String id) {
		codeListDetailPresenter.setData(id);
	}
	
	public void refresh(){
		this.codeListPresenter.refresh();
	}

	
}
