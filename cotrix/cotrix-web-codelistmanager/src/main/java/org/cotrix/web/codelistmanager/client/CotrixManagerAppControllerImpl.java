package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.client.presenter.CodeListManagerPresenter;
import org.cotrix.web.codelistmanager.client.view.CodeListManagerView;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class CotrixManagerAppControllerImpl implements CotrixManagerAppController{
	private ManagerServiceAsync rpcService;
	private HandlerManager eventBus;
	private CodeListManagerPresenter codeListManagerPresenter;
	
	@Inject
	public CotrixManagerAppControllerImpl(ManagerServiceAsync rpcService,HandlerManager eventBus,CodeListManagerPresenter codeListManagerPresenter) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.codeListManagerPresenter = codeListManagerPresenter;
	}
	
	public void go(HasWidgets container) {
		this.codeListManagerPresenter.go(container);
	}
	
	public void onValueChange(ValueChangeEvent<String> event) {
	}

	public void refresh() {
		this.codeListManagerPresenter.refresh();
	}

}
