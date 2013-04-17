package org.cotrix.web.publish.client.presenter;

import org.cotrix.web.publish.client.PublishServiceAsync;
import org.cotrix.web.publish.client.view.ChanelPropertyView;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class ChanelPropertyPresenterImpl implements ChanelPropertyPresenter {

	private PublishServiceAsync rpcService;
	private HandlerManager eventBus;
	private ChanelPropertyView view;
	
	@Inject
	public ChanelPropertyPresenterImpl(PublishServiceAsync rpcService,
			HandlerManager eventBus, ChanelPropertyView view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}
	public void show(){
		view.show();
	}

}
