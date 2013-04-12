package org.cotrix.web.publish.client;

import org.cotrix.web.publish.client.presenter.CodeListPublishPresenter;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class CotrixPublishAppControllerImpl implements CotrixPublishAppController{

	private PublishServiceAsync rpcService;
	private HandlerManager eventBus;
	private CodeListPublishPresenter codeListPublishPresenter;
	
	@Inject
	public CotrixPublishAppControllerImpl(PublishServiceAsync rpcService,HandlerManager eventBus,CodeListPublishPresenter codeListPubllishPresenter) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.codeListPublishPresenter = codeListPubllishPresenter;
	}
	

	
	public void go(HasWidgets container) {
		this.codeListPublishPresenter.go(container);
	}

	public void onValueChange(ValueChangeEvent<String> event) {
		
	}

}
