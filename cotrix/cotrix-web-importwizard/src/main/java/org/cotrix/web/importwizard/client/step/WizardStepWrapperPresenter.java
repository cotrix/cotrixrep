package org.cotrix.web.importwizard.client.step;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.Presenter;
import org.cotrix.web.share.shared.CotrixImportModelController;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;


public class WizardStepWrapperPresenter implements Presenter<WizardStepWrapperPresenter>, WizardStepWrapperView.Presenter<WizardStepWrapperPresenter>{

	private OnButtonClickHandler onButtonClickHandler ;
	public interface OnButtonClickHandler {
		boolean isFromValidated(WizardStepWrapperPresenter sender);
		void onNextButtonClicked(WizardStepWrapperPresenter sender);
		void onBackButtonClicked(WizardStepWrapperPresenter sender);
		void onSaveButtonClicked(WizardStepWrapperPresenter sender);
		void onUploadOtherButtonClicked(WizardStepWrapperPresenter sender);
		void onManageCodelistButtonClicked(WizardStepWrapperPresenter sender);
		
	}

	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final WizardStepWrapperView view;
	private CotrixImportModelController model;
	private final Presenter childPresenter;
	private final int index;
	
	@Inject
	public WizardStepWrapperPresenter(ImportServiceAsync rpcService, HandlerManager eventBus,WizardStepWrapperView view,CotrixImportModelController model,Presenter childPresenter,String title,int index){
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.model = model;
		this.view = view;
		this.childPresenter = childPresenter;
		this.view.setFormTitle(title);
		this.index = index;
	}
	
	public void SetOnButtonClickHandler(OnButtonClickHandler onButtonClickHandler){
		this. onButtonClickHandler = onButtonClickHandler;
	}
	
	public Presenter getContent(){
		return this.childPresenter;
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}
	
	public int getIndexInParent(){
		return this.index;
	}
	
	public void showBackButton(boolean isVisible){
		view.showBackButton(isVisible);
	}
	
	public void showNextButton(boolean isVisible){
		view.showNextButton(isVisible);
	}
	public void showSaveButton(boolean isVisible){
		view.showSaveButton(isVisible);
	}
	
	public void showUploadOtherButton(boolean isVisible){
		view.showUploadOtherButton(isVisible);
	}
	
	public void showManageCodelistButton(boolean isVisible){
		view.showManageCodelistButton(isVisible);
	}
	
	public void onNextButtonClicked() {
		if(onButtonClickHandler.isFromValidated(this)){
			onButtonClickHandler.onNextButtonClicked(this);
		}
	}

	public void onBackButtonClicked() {
		onButtonClickHandler.onBackButtonClicked(this);
	}

	public void addForm(HasWidgets container) {
		childPresenter.go(container);
	}

	public void onSaveButtonClicked() {
		onButtonClickHandler.onSaveButtonClicked(this);
	}

	public void onUploadOtherButtonClicked() {
		onButtonClickHandler.onUploadOtherButtonClicked(this);
	}

	public void onManageCodelistButtonClicked() {
		onButtonClickHandler.onManageCodelistButtonClicked(this);
	}

	public void onUploadFileFinish() {
		// TODO Auto-generated method stub
		
	}

}
