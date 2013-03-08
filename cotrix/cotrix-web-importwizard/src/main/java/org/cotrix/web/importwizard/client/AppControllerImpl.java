package org.cotrix.web.importwizard.client;

import org.cotrix.web.importwizard.client.presenter.ImportWizardPresenter;
import org.cotrix.web.importwizard.shared.CotrixImportModel;
import org.cotrix.web.importwizard.shared.CotrixImportModelController;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class AppControllerImpl implements AppController {
	private final HandlerManager eventBus;
	private final ImportServiceAsync rpcService;
	private HasWidgets container;
	private CotrixImportModelController model;
    private ImportWizardPresenter importWizardPresenter;

    @Inject
    public AppControllerImpl(ImportServiceAsync rpcService, HandlerManager eventBus,CotrixImportModelController model, ImportWizardPresenter importWizardPresenter) {
		this.eventBus = new HandlerManager(null);
		this.rpcService = rpcService;
		this.model = model;
        this.importWizardPresenter = importWizardPresenter;
		bind();
	}

	private void bind() {
		History.addValueChangeHandler(this);
	}

	public void go(HasWidgets container) {
		this.container = container;
		importWizardPresenter.go(container);
		/*if ("".equals(History.getToken())) {
			History.newItem("import");
		} else {
			History.fireCurrentHistoryState();
		}*/
	}

	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();
		if (token != null) {
			if (token.equals("import")) {
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
						
					}

					public void onSuccess() {
						importWizardPresenter.go(container);
					}
				});
			}
		}
	}
}
