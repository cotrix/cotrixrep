package org.cotrix.web.importwizard.client;

import com.google.inject.Inject;
import org.cotrix.web.importwizard.client.presenter.*;
import org.cotrix.web.importwizard.client.presenter.UploadFormPresenterImpl;
import org.cotrix.web.importwizard.client.view.form.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

public class AppControllerImpl implements AppController {
	private final HandlerManager eventBus;
	private final ImportServiceAsync rpcService;
	private HasWidgets container;
    private ImportWizardPresenter importWizardPresenter;

    @Inject
    public AppControllerImpl(ImportServiceAsync rpcService, HandlerManager eventBus,ImportWizardPresenter importWizardPresenter) {
		this.eventBus = new HandlerManager(null);
		this.rpcService = rpcService;
        this.importWizardPresenter = importWizardPresenter;
		bind();
	}

	private void bind() {
		History.addValueChangeHandler(this);
	}

	public void go(HasWidgets container) {
		this.container = container;
		if ("".equals(History.getToken())) {
			History.newItem("import");
		} else {
			History.fireCurrentHistoryState();
		}
	}

	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();
		if (token != null) {
			if (token.equals("import")) {
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
					}

					public void onSuccess() {
//						GenericImportWizardPresenterImpl importWizardPresenter =  new GenericImportWizardPresenterImpl(rpcService, eventBus, importWizardView);
//						importWizardPresenter.addForm(getUploadForm(), "Upload CSV File");
//						importWizardPresenter.addForm(getMetadataForm(),"Add Metadata");
//						importWizardPresenter.addForm(getHeaderSelectionForm(),"Select Header");
//						importWizardPresenter.addForm(getHeaderDescriptionForm(),"Describe Header");
//						importWizardPresenter.addForm(getHeaderTypeForm(),"Define Header Type");
//						importWizardPresenter.addForm(getSummaryForm(),"Summary");
//						importWizardView.setPresenter(importWizardPresenter);
						importWizardPresenter.go(container);
					}
				});
			}
		}
	}
}
