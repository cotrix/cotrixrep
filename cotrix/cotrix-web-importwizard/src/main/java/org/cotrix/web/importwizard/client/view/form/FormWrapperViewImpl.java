package org.cotrix.web.importwizard.client.view.form;

import org.cotrix.web.importwizard.client.presenter.FormWrapperPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.PreElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class FormWrapperViewImpl extends Composite implements FormWrapperView<FormWrapperViewImpl>{

	private static FormWrapperUiBinder uiBinder = GWT.create(FormWrapperUiBinder.class);

	@UiTemplate("FormWrapper.ui.xml")
	interface FormWrapperUiBinder extends UiBinder<Widget, FormWrapperViewImpl> {}

	@UiField FlowPanel contentPanel;
	@UiField Button nextButton;
	@UiField Button saveButton;
	@UiField Label title;
	@UiField Button backButton;
	@UiField Button uploadOtherButton;
	@UiField Button manageCodelistButton;
	
	private Presenter<FormWrapperPresenter> presenter;
	public void setPresenter(Presenter<FormWrapperPresenter> presenter) {
		this.presenter = presenter;
		addForm();
	}
	
	public FormWrapperViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	
	@UiHandler("nextButton")
	public void onNextButtonClicked(ClickEvent event){
		presenter.onNextButtonClicked();
	}
	
	@UiHandler("saveButton")
	public void onSaveButtonClicked(ClickEvent event) {
		presenter.onSaveButtonClicked();
	}
	
	@UiHandler("backButton")
	public void onBackButtonClicked(ClickEvent event){
		presenter.onBackButtonClicked();
	}
	
	@UiHandler("uploadOtherButton")
	public void onUploadOtherButtonClicked(ClickEvent event) {
		presenter.onUploadOtherButtonClicked();
	}

	@UiHandler("manageCodelistButton")
	public void onManageCodelistButtonClicked(ClickEvent event) {
		presenter.onManageCodelistButtonClicked();
	}
	
	
	public void addForm() {
		presenter.addForm(contentPanel);
	}

	public void setFormTitle(String title) {
		this.title.setText(title);
	}

	public void showBackButton(boolean isVisible) {
		this.backButton.setVisible(isVisible);
	}

	public void showNextButton(boolean isVisible) {
		this.nextButton.setVisible(isVisible);
	}

	public void showSaveButton(boolean isVisible) {
		this.saveButton.setVisible(isVisible);
	}

	public void showUploadOtherButton(boolean isVisible) {
		this.uploadOtherButton.setVisible(isVisible);
	}

	public void showManageCodelistButton(boolean isVisible) {
		this.manageCodelistButton.setVisible(isVisible);
	}

}
