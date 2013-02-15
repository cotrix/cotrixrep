package org.cotrix.web.importwizard.client;

import java.util.ArrayList;

import org.cotrix.web.importwizard.client.form.DescribeHeaderForm;
import org.cotrix.web.importwizard.client.form.FormWrapper;
import org.cotrix.web.importwizard.client.form.FormWrapper.OnButtonClickHandler;
import org.cotrix.web.importwizard.client.form.HeaderSelectionForm;
import org.cotrix.web.importwizard.client.form.HeaderTypeForm;
import org.cotrix.web.importwizard.client.form.MetadataForm;
import org.cotrix.web.importwizard.client.form.SummaryForm;
import org.cotrix.web.importwizard.client.form.UploadFrom;
import org.cotrix.web.importwizard.client.progressbar.ProgressbarTracker;
import org.cotrix.web.importwizard.shared.ImportWizardModel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class ImportWizard extends Composite implements
		OnButtonClickHandler {

	private static ImportWizardUiBinder uiBinder = GWT
			.create(ImportWizardUiBinder.class);

	interface ImportWizardUiBinder extends UiBinder<Widget, ImportWizard> {
	}

	@UiField
	FlowPanel barPanel;

	@UiField
	DeckPanel formPanel;
	private ProgressbarTracker mProgressbarTracker;

	private ArrayList<String[]> csvFile = new ArrayList<String[]>();
	
	public ImportWizard() {
		initWidget(uiBinder.createAndBindUi(this));
		String[] labels = new String[] { "Upload File", "Add Metadata",
				"Choose Header", "Describe Header", "Define Type", "Done" };

		String[] titles = new String[] { "Upload CSV File", "Add Metadata",
				"Choose Header", "Describe Header", "Define Type", "Summary" };

		mProgressbarTracker = new ProgressbarTracker(6, labels);
		barPanel.add(mProgressbarTracker);
		ImportWizardModel importWizardModel = new ImportWizardModel();

		FormWrapper wrapper1 = FormWrapper.getInstanceWithoutBackButton(getFormInStep1(importWizardModel), titles[0], 0);
		FormWrapper wrapper2 = FormWrapper.getInstance(getFormInStep2(importWizardModel),titles[1], 1);
		FormWrapper wrapper3 = FormWrapper.getInstance(getFormInStep3(importWizardModel),titles[2], 2);
		FormWrapper wrapper4 = FormWrapper.getInstance(getFormInStep4(importWizardModel),titles[3], 3);
		FormWrapper wrapper5 = FormWrapper.getInstance(getFormInStep5(importWizardModel),titles[4], 4);
		FormWrapper wrapper6 = FormWrapper.getInstanceWithoutNextButton(getFormInStep6(importWizardModel), titles[5], 5);

		wrapper1.setOnButtonClicked(this);
		wrapper2.setOnButtonClicked(this);
		wrapper3.setOnButtonClicked(this);
		wrapper4.setOnButtonClicked(this);
		wrapper5.setOnButtonClicked(this);
		wrapper6.setOnButtonClicked(this);

		formPanel.add(wrapper1);
		formPanel.add(wrapper2);
		formPanel.add(wrapper3);
		formPanel.add(wrapper4);
		formPanel.add(wrapper5);
		formPanel.add(wrapper6);
		formPanel.showWidget(0);

	}

	public ImportWizard(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	private Widget getFormInStep1(ImportWizardModel importWizardModel) {
		UploadFrom uploadFrom = new UploadFrom(importWizardModel);
		return  uploadFrom;
	}

	private Widget getFormInStep2(ImportWizardModel importWizardModel) {
		return new MetadataForm(importWizardModel);
	}

	private Widget getFormInStep3(ImportWizardModel importWizardModel) {
		return  new HeaderSelectionForm(importWizardModel);
	}

	private Widget getFormInStep4(ImportWizardModel importWizardModel) {
		return new DescribeHeaderForm(importWizardModel);
	}

	private Widget getFormInStep5(ImportWizardModel importWizardModel) {
		String[] columns = new String[]{"ISSCAAP","TAXOCODE","3A_CODE","Scientific Name","English Name","French Name","Spanish Name","Family","Author Name"};
		return new HeaderTypeForm(importWizardModel);
	}

	private Widget getFormInStep6(ImportWizardModel importWizardModel) {
		return new SummaryForm(importWizardModel);
	}

	public boolean isFromValidated(FormWrapper sender) {
		int index = sender.getIndexInParent();
		boolean isValidated = false;
		switch (index) {
		case 0:
			UploadFrom uploadFrom = (UploadFrom) sender.getContent();
			isValidated = uploadFrom.isValidate() ;
			break;
		case 1:
			MetadataForm metadataForm = (MetadataForm) sender.getContent();
			isValidated = metadataForm.isValidate() ;
			break;
		case 2:
			HeaderSelectionForm headerSelectionForm = (HeaderSelectionForm) sender.getContent();
			isValidated = headerSelectionForm.isValidate() ;
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

	public void onNextButtonClicked(FormWrapper sender) {
		int index = sender.getIndexInParent();
		formPanel.showWidget(index + 1);
		mProgressbarTracker.setActiveIndex(index + 2);
	}

	public void onBackButtonClicked(FormWrapper sender) {
		int index = sender.getIndexInParent();
		formPanel.showWidget(index -1);
		mProgressbarTracker.setActiveIndex(index);
	}

}
