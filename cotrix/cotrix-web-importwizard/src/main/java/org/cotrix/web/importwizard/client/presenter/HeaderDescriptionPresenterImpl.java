package org.cotrix.web.importwizard.client.presenter;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.form.HeaderDescriptionFormView;
import org.cotrix.web.importwizard.client.view.form.HeaderDescriptionFormViewImpl;
import org.cotrix.web.importwizard.client.view.form.FormWrapperView;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class HeaderDescriptionPresenterImpl implements HeaderDescriptionPresenter {
	
	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final HeaderDescriptionFormView view;
	
	@Inject
	public HeaderDescriptionPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus,HeaderDescriptionFormView view){
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
