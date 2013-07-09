package org.cotrix.web.importwizard.client;

import org.cotrix.web.importwizard.client.progresstracker.ProgressTracker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class ImportWizardViewImpl extends Composite implements ImportWizardView {

	@UiTemplate("ImportWizard.ui.xml")
	interface ImportWizardUiBinder extends UiBinder<Widget, ImportWizardViewImpl> {}
	private static ImportWizardUiBinder uiBinder = GWT.create(ImportWizardUiBinder.class);

	@UiField FlowPanel progressTracker;
	@UiField DeckPanel formPanel;

	private ProgressTracker mProgressbarTracker;

	private Presenter presenter;
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public ImportWizardViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void initProgressBarTracker(){
		String[] labels = new String[] { 
				"Upload File",
				"Add Metadata",
				"Select Header",
				"Define Type",
				"Summary",
				"Done" };
		mProgressbarTracker = new ProgressTracker(6, labels);
		progressTracker.add(mProgressbarTracker);
	}

	public void initForm(){
		presenter.initForm(formPanel);
		formPanel.showWidget(0);
	}

	public void showNextStep(int index) {
		formPanel.showWidget(index + 1);
		mProgressbarTracker.setActiveIndex(index + 2);		
	}

	public void showPrevStep(int index) {
		formPanel.showWidget(index -1);
		mProgressbarTracker.setActiveIndex(index);
	}



}
