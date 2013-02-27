package org.cotrix.web.importwizard.client.view.form;

import org.cotrix.web.importwizard.client.presenter.UploadFormPresenterImpl;
import org.vectomatic.file.FileUploadExt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class UploadFormViewImpl extends Composite implements UploadFormView<UploadFormViewImpl> {

	private static UploadFromUiBinder uiBinder = GWT.create(UploadFromUiBinder.class);

	@UiTemplate("UploadForm.ui.xml")
	interface UploadFromUiBinder extends UiBinder<Widget, UploadFormViewImpl> {}

	public UploadFormViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	private Presenter<UploadFormPresenterImpl> presenter;
	public void setPresenter(Presenter<UploadFormPresenterImpl> presenter) {
		this.presenter = presenter;
	}

	@UiField FileUploadExt fileUploadButton;
	@UiField Label fileNameLabel;
	@UiField HTML deleteButton;
	@UiField FlowPanel fileWrapperPanel;
	@UiField Button browseButton;

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
}
