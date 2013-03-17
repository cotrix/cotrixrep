package org.cotrix.web.codelistmanager.client.presenter;

import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.client.view.CodeListView;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class CodeListPresenterImpl implements CodeListPresenter {
	private ManagerServiceAsync rpcService;
	private HandlerManager eventBus;
	private CodeListView view;

	@Inject
	public CodeListPresenterImpl(ManagerServiceAsync rpcService,HandlerManager eventBus, CodeListView view) {
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
