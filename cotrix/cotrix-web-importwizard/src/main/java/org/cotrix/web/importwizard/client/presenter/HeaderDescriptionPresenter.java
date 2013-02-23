package org.cotrix.web.importwizard.client.presenter;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.form.HeaderDescriptionFormView;
import org.cotrix.web.importwizard.client.view.form.HeaderDescriptionFormViewImpl;
import org.cotrix.web.importwizard.client.view.form.FormWrapperView;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;

public class HeaderDescriptionPresenter implements Presenter<HeaderDescriptionPresenter>,HeaderDescriptionFormView.Presenter<HeaderDescriptionPresenter> {
	
	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final HeaderDescriptionFormView<HeaderDescriptionFormViewImpl> view;
	
	public HeaderDescriptionPresenter(ImportServiceAsync rpcService, HandlerManager eventBus,HeaderDescriptionFormView<HeaderDescriptionFormViewImpl> view){
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		String[] headers = new String[]{"TAXOcoDE","ISSCAGrOUP"};
		view.initForm(headers);
	}

}
