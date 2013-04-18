package org.cotrix.web.publish.client.presenter;

import org.cotrix.web.publish.client.PublishServiceAsync;
import org.cotrix.web.publish.client.view.ChanelPropertyDialogView;
import org.cotrix.web.publish.shared.ChanelPropertyModelController;
import org.cotrix.web.publish.shared.ChanelPropertyModelController.OnChanelPropertyValidated;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class ChanelPropertyDialogPresenterImpl implements ChanelPropertyDialogPresenter {

	private PublishServiceAsync rpcService;
	private HandlerManager eventBus;
	private ChanelPropertyDialogView view;
	private ChanelPropertyModelController model;
	
	@Inject
	public ChanelPropertyDialogPresenterImpl(PublishServiceAsync rpcService,
			HandlerManager eventBus, ChanelPropertyDialogView view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}
	
	public void setModel(ChanelPropertyModelController model){
		this.model = model;
	}

	public void onDoneButtonClicked(String url,String username,String password) {
		this.model.getChanelProperty().setUrl(url);
		this.model.getChanelProperty().setUsername(username);
		this.model.getChanelProperty().setPassword(password);
		this.model.validate();
		view.hide();
	}

	public void show() {
		view.show();
	}
}
