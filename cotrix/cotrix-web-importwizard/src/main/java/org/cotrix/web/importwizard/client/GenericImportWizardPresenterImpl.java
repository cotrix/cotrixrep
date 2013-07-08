package org.cotrix.web.importwizard.client;

import java.util.ArrayList;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.step.FormWrapperPresenter;
import org.cotrix.web.importwizard.client.step.FormWrapperViewImpl;
import org.cotrix.web.importwizard.client.step.done.DoneFormPresenter;
import org.cotrix.web.importwizard.client.step.mapping.HeaderTypeFormPresenterImpl;
import org.cotrix.web.importwizard.client.step.metadata.MetadataFormPresenterImpl;
import org.cotrix.web.importwizard.client.step.preview.HeaderSelectionFormPresenterImpl;
import org.cotrix.web.importwizard.client.step.upload.UploadFormPresenterImpl;
import org.cotrix.web.share.shared.CotrixImportModelController;
import org.cotrix.web.share.shared.HeaderType;
import org.cotrix.web.share.shared.Metadata;
import org.cotrix.web.share.shared.Metadata;
import org.cotrix.web.share.shared.Metadata;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;


public class GenericImportWizardPresenterImpl implements ImportWizardPresenter {
	
	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final ImportWizardView view;
	private CotrixImportModelController model;
	private ArrayList<Presenter<GenericImportWizardPresenterImpl>> presenters  = new ArrayList<Presenter<GenericImportWizardPresenterImpl>>();
	private ArrayList<String> formLabel  = new ArrayList<String>();
	private UploadFormPresenterImpl uploadFormPresenter;
	private DoneFormPresenter doneFormPresenter ;
    @Inject
	public GenericImportWizardPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus, ImportWizardView view,CotrixImportModelController model) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.model = model;
        this.view.setPresenter(this);
	}	
	
	public void go(HasWidgets container) {
//		container.clear();
		container.add(view.asWidget());
		this.view.initProgressBarTracker();
		this.view.initForm();
	}
	
	public void addForm(Presenter presenter,String formLabel){
		this.presenters.add(presenter);
		this.formLabel.add(formLabel);
	}

	public void initForm(HasWidgets container) {
		for (int i = 0; i < presenters.size(); i++) {
			FormWrapperViewImpl formWrapperView = new FormWrapperViewImpl();
			FormWrapperPresenter formWrapperPresenter = new FormWrapperPresenter(rpcService, eventBus, formWrapperView,model,presenters.get(i),formLabel.get(i),i);
			formWrapperView.setPresenter(formWrapperPresenter);
			formWrapperPresenter.SetOnButtonClickHandler(this);
			formWrapperPresenter.go(container);
			formWrapperPresenter.showSaveButton(false);
			
			if(i == 0){
				formWrapperPresenter.showBackButton(false);
				formWrapperPresenter.showUploadOtherButton(false);
				formWrapperPresenter.showManageCodelistButton(false);				
			}
			if(i == presenters.size()-2){
				formWrapperPresenter.showNextButton(false);
				formWrapperPresenter.showSaveButton(true);
				formWrapperPresenter.showUploadOtherButton(false);
				formWrapperPresenter.showManageCodelistButton(false);				
			}
			if(i== presenters.size()-1){
				formWrapperPresenter.showNextButton(false);
				formWrapperPresenter.showSaveButton(false);
				formWrapperPresenter.showBackButton(false);
				formWrapperPresenter.showUploadOtherButton(true);
				formWrapperPresenter.showManageCodelistButton(true);
				
			    doneFormPresenter = (DoneFormPresenter) formWrapperPresenter.getContent();
			}
		}
	}

	public boolean isFromValidated(FormWrapperPresenter sender) {
		int index = sender.getIndexInParent();
		boolean isValidated = false;
		switch (index) {
		case 0:
			uploadFormPresenter =   (UploadFormPresenterImpl) sender.getContent();
			isValidated = uploadFormPresenter.isValid();
			break;
		case 1:
			MetadataFormPresenterImpl metadataFormPresenter =   (MetadataFormPresenterImpl) sender.getContent();
			isValidated = metadataFormPresenter.isValid();
			break;
		case 2:
			HeaderSelectionFormPresenterImpl headerSelectionFormPresenter =   (HeaderSelectionFormPresenterImpl) sender.getContent();
			isValidated = headerSelectionFormPresenter.isValid();
			break;
	/*	case 3:
			HeaderDescriptionPresenterImpl headerDescriptionPresenter =   (HeaderDescriptionPresenterImpl) sender.getContent();
			isValidated = headerDescriptionPresenter.isValidated();
			break;*/
		case 3:
			HeaderTypeFormPresenterImpl headerTypeFormPresenter =   (HeaderTypeFormPresenterImpl) sender.getContent();
			isValidated = headerTypeFormPresenter.isValid();
			break;
		case 4:
			isValidated = true;
			break;
		case 5:
			isValidated = true;
			break;
		}
		return isValidated;
	}

	public void onNextButtonClicked(FormWrapperPresenter sender) {
		view.showNextStep(sender.getIndexInParent());
	}

	public void onBackButtonClicked(FormWrapperPresenter sender) {
		view.showPrevStep(sender.getIndexInParent());
	}

	public void onSaveButtonClicked(FormWrapperPresenter sender) {
		this.uploadFormPresenter.submitForm();
		this.uploadFormPresenter.setOnUploadFileFinish(this);
	}

	public void onUploadOtherButtonClicked(FormWrapperPresenter sender) {
		uploadFormPresenter.reset();
		model = new CotrixImportModelController();
		view.showPrevStep(1);
	}

	public void onManageCodelistButtonClicked(FormWrapperPresenter sender) {
		Window.alert("Go to manage codelist");
	}

	public void uploadFileFinish(SubmitCompleteEvent event) {
		view.showPrevStep(presenters.size());
		doneFormPresenter.setDoneTitle("Successful upload file to server !!!");
		doneFormPresenter.setWarningMessage(event.getResults());
	}



}
