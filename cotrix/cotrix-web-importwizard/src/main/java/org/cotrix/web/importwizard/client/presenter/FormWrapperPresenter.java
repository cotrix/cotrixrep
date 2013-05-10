package org.cotrix.web.importwizard.client.presenter;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.form.FormWrapperView;
import org.cotrix.web.share.shared.CotrixImportModelController;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;


public class FormWrapperPresenter implements Presenter<FormWrapperPresenter>, FormWrapperView.Presenter<FormWrapperPresenter>{

	private OnButtonClickHandler onButtonClickHandler ;
	public interface OnButtonClickHandler {
		boolean isFromValidated(FormWrapperPresenter sender);
		void onNextButtonClicked(FormWrapperPresenter sender);
		void onBackButtonClicked(FormWrapperPresenter sender);
		void onSaveButtonClicked(FormWrapperPresenter sender);
		void onUploadOtherButtonClicked(FormWrapperPresenter sender);
		void onManageCodelistButtonClicked(FormWrapperPresenter sender);
		
	}

	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final FormWrapperView view;
	private CotrixImportModelController model;
	private final Presenter childPresenter;
	private final int index;
	
	@Inject
	public FormWrapperPresenter(ImportServiceAsync rpcService, HandlerManager eventBus,FormWrapperView view,CotrixImportModelController model,Presenter childPresenter,String title,int index){
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

}
