package org.cotrix.web.importwizard.client.step.upload;

import org.cotrix.web.importwizard.client.util.AlertDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UploadStepViewImpl extends Composite implements UploadStepView<UploadStepViewImpl> {

	private static UploadStepUiBinder uiBinder = GWT.create(UploadStepUiBinder.class);

	@UiTemplate("UploadStep.ui.xml")
	interface UploadStepUiBinder extends UiBinder<Widget, UploadStepViewImpl> {}

	public UploadStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		form.setAction(GWT.getModuleBaseURL()+"fileupload");
		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
//				alert(event.getResults());
				presenter.onSubmitComplete(event);
			}
		});
	}

	private Presenter<UploadStepPresenterImpl> presenter;
	public void setPresenter(Presenter<UploadStepPresenterImpl> presenter) {
		this.presenter = presenter;
	}

	@UiField FileUploadExt fileUploadButton;
	@UiField Label fileNameLabel;
	@UiField HTML deleteButton;
	@UiField FlowPanel fileWrapperPanel;
	@UiField Button browseButton;
	@UiField FormPanel form;
	@UiField Hidden cotrixmodelField;

	private AlertDialog alertDialog;

	@UiHandler("deleteButton")
	public void onDeleteButtonClicked(ClickEvent event) {
		presenter.onDeleteButtonClicked();
	}

	@UiHandler("browseButton")
	public void onBrowseButtonClicked(ClickEvent event) {
		presenter.onBrowseButtonClicked();
	}

	@UiHandler("fileUploadButton")
	public void onUploadFileChange(ChangeEvent event) {
		presenter.onUploadFileChange(fileUploadButton.getFiles(),fileUploadButton.getFilename());
	}
	 
	public void setFileUploadButtonClicked() {
		this.fileUploadButton.click();
	}

	public void setOnUploadFinish(String filename) {
		this.fileNameLabel.setText(filename);
		this.deleteButton.setVisible(true);
		this.fileWrapperPanel.setVisible(true);
	}

	public void setOnDeleteButtonClicked() {
		this.fileNameLabel.setText("");
		this.fileWrapperPanel.setVisible(false);
		this.deleteButton.setVisible(false);
	}

	public void alert(String message) {
		if(alertDialog == null){
			alertDialog = new AlertDialog();
		}
		alertDialog.setMessage(message);
		alertDialog.show();
	}

	public void submitForm() {
		this.form.submit();
	}

	public void setCotrixModelFieldValue(String model) {
		this.cotrixmodelField.setDefaultValue(model);
	}

	public void reset() {
		setOnDeleteButtonClicked();
		this.form.reset();
	}


}
