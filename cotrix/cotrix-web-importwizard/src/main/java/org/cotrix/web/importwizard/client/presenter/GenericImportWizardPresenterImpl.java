package org.cotrix.web.importwizard.client.presenter;

import java.util.ArrayList;

import com.google.inject.Inject;
import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.ImportWizardView;
import org.cotrix.web.importwizard.client.view.form.FormWrapperViewImpl;
import org.cotrix.web.importwizard.shared.CotrixImportModel;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;


public class GenericImportWizardPresenterImpl implements ImportWizardPresenter {
	
	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final ImportWizardView view;
	private CotrixImportModel model;
	private ArrayList<Presenter<GenericImportWizardPresenterImpl>> presenters  = new ArrayList<Presenter<GenericImportWizardPresenterImpl>>();
	private ArrayList<String> formLabel  = new ArrayList<String>();

    @Inject
	public GenericImportWizardPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus, ImportWizardView view,CotrixImportModel model) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.model = model;
        this.view.setPresenter(this);
	}	
	
	public void go(HasWidgets container) {
		container.clear();
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
			
			if(i == 0){
				formWrapperPresenter.showBackButton(false);
			}
			if(i == presenters.size()-1){
				formWrapperPresenter.showNextButton(false);
			}
		}
	}

	public boolean isFromValidated(FormWrapperPresenter sender) {
		int index = sender.getIndexInParent();
		boolean isValidated = false;
		switch (index) {
		case 0:
			UploadFormPresenterImpl uploadFormPresenter =   (UploadFormPresenterImpl) sender.getContent();
			isValidated = uploadFormPresenter.isValidated();
			break;
		case 1:
			MetadataFormPresenterImpl metadataFormPresenter =   (MetadataFormPresenterImpl) sender.getContent();
			isValidated = metadataFormPresenter.isValidated();
			break;
		case 2:
			HeaderSelectionFormPresenterImpl headerSelectionFormPresenter =   (HeaderSelectionFormPresenterImpl) sender.getContent();
			isValidated = headerSelectionFormPresenter.isValidated();
			
			break;
		case 3:
			isValidated = true;
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


}
