package org.cotrix.web.importwizard.client.view.form;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.importwizard.client.presenter.*;
import org.cotrix.web.importwizard.client.view.form.FormWrapperView.Presenter;
import org.cotrix.web.importwizard.shared.ImportWizardModel;
import org.vectomatic.file.ErrorCode;
import org.vectomatic.file.File;
import org.vectomatic.file.FileError;
import org.vectomatic.file.FileList;
import org.vectomatic.file.FileReader;
import org.vectomatic.file.FileUploadExt;
import org.vectomatic.file.events.ErrorEvent;
import org.vectomatic.file.events.ErrorHandler;
import org.vectomatic.file.events.LoadEndEvent;
import org.vectomatic.file.events.LoadEndHandler;
import org.vectomatic.file.events.LoadStartEvent;
import org.vectomatic.file.events.LoadStartHandler;
import org.vectomatic.file.events.ProgressEvent;
import org.vectomatic.file.events.ProgressHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class UploadFormViewImpl extends Composite implements UploadFormView<UploadFormViewImpl> {

	private static UploadFromUiBinder uiBinder = GWT.create(UploadFromUiBinder.class);

	@UiTemplate("UploadForm.ui.xml")
	interface UploadFromUiBinder extends UiBinder<Widget, UploadFormViewImpl> {}

	public UploadFormViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	private Presenter presenter;
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@UiField FileUploadExt fileBrowserButton;
	@UiField Label fileNameLabel;
	@UiField HTML deleteButton;
	@UiField Button browseButton;

	@UiHandler("deleteButton")
	public void onDeleteButtonClicked(ClickEvent event) {
		presenter.onDeleteButtonClicked();
	}

	@UiHandler("browseButton")
	public void onBrowseButtonClicked(ClickEvent event) {
		presenter.onBrowseButtonClicked();
	}

	public void setPresenter(UploadFormPresenterImpl uploadFormPresenter) {
		
	}

}
