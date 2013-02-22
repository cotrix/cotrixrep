package org.cotrix.web.importwizard.client.presenter;

import java.util.ArrayList;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.ImportWizardView;
import org.cotrix.web.importwizard.client.view.ImportWizardViewImpl;
import org.cotrix.web.importwizard.client.view.form.FormWrapperViewImpl;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;


public class ImportWizardPresenter implements Presenter<ImportWizardPresenter>, ImportWizardView.Presenter<ImportWizardPresenter>,FormWrapperPresenter.OnButtonClickHandler{
	
	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final ImportWizardView<ImportWizardViewImpl> view;
	private ArrayList<Presenter<ImportWizardPresenter>> presenters  = new ArrayList<Presenter<ImportWizardPresenter>>();
	private ArrayList<String> formLabel  = new ArrayList<String>();

	public ImportWizardPresenter(ImportServiceAsync rpcService, HandlerManager eventBus, ImportWizardView<ImportWizardViewImpl> view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
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
			FormWrapperPresenter formWrapperPresenter = new FormWrapperPresenter(rpcService, eventBus, formWrapperView,presenters.get(i),formLabel.get(i),i);
			formWrapperPresenter.go(container);
			formWrapperPresenter.SetOnButtonClickHandler(this);
			formWrapperView.setPresenter(formWrapperPresenter);
			
			if(i == 0){
				formWrapperPresenter.showBackButton(false);
			}
			if(i == presenters.size()-1){
				formWrapperPresenter.showNextButton(false);
			}
		}
	}

	public boolean isFromValidated(FormWrapperPresenter sender) {
		return true;
	}

	public void onNextButtonClicked(FormWrapperPresenter sender) {
		view.showNextStep(sender.getIndexInParent());
	}

	public void onBackButtonClicked(FormWrapperPresenter sender) {
		view.showPrevStep(sender.getIndexInParent());
	}


}
