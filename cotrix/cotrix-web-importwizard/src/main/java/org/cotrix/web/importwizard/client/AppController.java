package org.cotrix.web.importwizard.client;

import org.cotrix.web.importwizard.client.presenter.HeaderDescriptionPresenter;
import org.cotrix.web.importwizard.client.presenter.HeaderSelectionFormPresenter;
import org.cotrix.web.importwizard.client.presenter.HeaderTypeFormPresenter;
import org.cotrix.web.importwizard.client.presenter.ImportWizardPresenter;
import org.cotrix.web.importwizard.client.presenter.MetadataFormPresenter;
import org.cotrix.web.importwizard.client.presenter.Presenter;
import org.cotrix.web.importwizard.client.presenter.SummaryFormPresenter;
import org.cotrix.web.importwizard.client.presenter.UploadFormPresenter;
import org.cotrix.web.importwizard.client.view.ImportWizardViewImpl;
import org.cotrix.web.importwizard.client.view.form.HeaderDescriptionFormViewImpl;
import org.cotrix.web.importwizard.client.view.form.HeaderSelectionFormViewImpl;
import org.cotrix.web.importwizard.client.view.form.HeaderTypeFormViewImpl;
import org.cotrix.web.importwizard.client.view.form.MetadataFormViewImpl;
import org.cotrix.web.importwizard.client.view.form.SummaryFormViewImpl;
import org.cotrix.web.importwizard.client.view.form.UploadFromViewImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

public class AppController implements Presenter, ValueChangeHandler<String> {
	private final HandlerManager eventBus;
	private final ImportServiceAsync rpcService;
	private ImportWizardViewImpl importWizardView = null;
	private HasWidgets container;

	public AppController(ImportServiceAsync rpcService,
			HandlerManager eventBus) {
		this.eventBus = eventBus;
		this.rpcService = rpcService;
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

	private Presenter<UploadFormPresenter> getUploadForm(){
		UploadFromViewImpl uploadFromView  = new UploadFromViewImpl();
		UploadFormPresenter uploadFormPresenter = new UploadFormPresenter(rpcService, eventBus, uploadFromView);
		uploadFromView.setPresenter(uploadFormPresenter);
		return uploadFormPresenter;
	}

	private Presenter<MetadataFormPresenter> getMetadataForm(){
		MetadataFormViewImpl metadataFormView  = new MetadataFormViewImpl();
		MetadataFormPresenter metadataFormPresenter = new MetadataFormPresenter(rpcService, eventBus, metadataFormView);
		metadataFormView.setPresenter(metadataFormPresenter);
		return metadataFormPresenter;
	}

	private Presenter<HeaderSelectionFormPresenter> getHeaderSelectionForm(){
		HeaderSelectionFormViewImpl headerSelectionFormView = new HeaderSelectionFormViewImpl();
		HeaderSelectionFormPresenter headerSelectionFormPresenter = new HeaderSelectionFormPresenter(rpcService, eventBus, headerSelectionFormView);
		headerSelectionFormView.setPresenter(headerSelectionFormPresenter);
		return headerSelectionFormPresenter;
	}

	private Presenter<HeaderDescriptionPresenter> getHeaderDescriptionForm(){
		HeaderDescriptionFormViewImpl describeHeaderFormView = new HeaderDescriptionFormViewImpl();
		HeaderDescriptionPresenter describeHeaderPresenter = new HeaderDescriptionPresenter(rpcService, eventBus, describeHeaderFormView);
		describeHeaderFormView.setPresenter(describeHeaderPresenter);
		return describeHeaderPresenter;
	}
	private Presenter<HeaderTypeFormPresenter> getHeaderTypeForm(){
		HeaderTypeFormViewImpl headerTypeFormView = new HeaderTypeFormViewImpl();
		HeaderTypeFormPresenter headerTypeFormPresenter = new HeaderTypeFormPresenter(rpcService, eventBus,headerTypeFormView );
		headerTypeFormView.setPresenter(headerTypeFormPresenter);
		return headerTypeFormPresenter;
	}

	private Presenter<SummaryFormPresenter> getSummaryForm(){
		SummaryFormViewImpl summaryFormView = new SummaryFormViewImpl();
		SummaryFormPresenter summaryFormPresenter = new SummaryFormPresenter(rpcService, eventBus,summaryFormView );
		summaryFormView.setPresenter(summaryFormPresenter);
		return summaryFormPresenter;
	}


	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();
		if (token != null) {
			if (token.equals("import")) {
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
					}

					public void onSuccess() {
						if (importWizardView == null)
							importWizardView = new ImportWizardViewImpl();
						
						ImportWizardPresenter importWizardPresenter =  new ImportWizardPresenter(rpcService, eventBus, importWizardView);
						importWizardPresenter.addForm(getUploadForm(),"Upload CSV File");
						importWizardPresenter.addForm(getMetadataForm(),"Add Metadata");
						importWizardPresenter.addForm(getHeaderSelectionForm(),"Select Header");
						importWizardPresenter.addForm(getHeaderDescriptionForm(),"Describe Header");
						importWizardPresenter.addForm(getHeaderTypeForm(),"Define Header Type");
						importWizardPresenter.addForm(getSummaryForm(),"Summary");
						importWizardView.setPresenter(importWizardPresenter);
						importWizardPresenter.go(container);
					}
				});
			}
		}
	}
}
