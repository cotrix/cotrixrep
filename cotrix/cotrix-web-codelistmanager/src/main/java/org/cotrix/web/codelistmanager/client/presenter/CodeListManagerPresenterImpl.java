package org.cotrix.web.codelistmanager.client.presenter;

import org.cotrix.web.codelistmanager.client.ManagerService;
import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.client.view.CodeListManagerView;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class CodeListManagerPresenterImpl implements CodeListManagerPresenter{
	private ManagerServiceAsync rpcService;
	private HandlerManager eventBus;
	private CodeListManagerView view;
	
	@Inject
	public CodeListManagerPresenterImpl(ManagerServiceAsync rpcService,HandlerManager eventBus,CodeListManagerView view){
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
	
}
