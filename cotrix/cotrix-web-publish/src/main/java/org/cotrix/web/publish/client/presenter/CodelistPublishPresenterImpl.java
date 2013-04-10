package org.cotrix.web.publish.client.presenter;

import org.cotrix.web.publish.client.PublishServiceAsync;
import org.cotrix.web.publish.client.view.CodelistPublishView;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class CodelistPublishPresenterImpl implements CodelistPublishPresenter {
	private PublishServiceAsync rpcService;
	private HandlerManager eventBus;
	private CodelistPublishView view;
	
	@Inject
	public CodelistPublishPresenterImpl(PublishServiceAsync rpcService,HandlerManager eventBus, CodelistPublishView view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

}
