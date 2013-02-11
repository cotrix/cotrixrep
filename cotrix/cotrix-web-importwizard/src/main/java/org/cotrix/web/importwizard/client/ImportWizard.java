package org.cotrix.web.importwizard.client;

import org.cotrix.web.importwizard.client.form.FormWrapper;
import org.cotrix.web.importwizard.client.form.FormWrapper.OnButtonClickHandler;
import org.cotrix.web.importwizard.client.progressbar.ProgressbarTracker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
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

	public ImportWizard() {
		initWidget(uiBinder.createAndBindUi(this));
		String[] labels = new String[] { "Upload File", "Add Metadata",
				"Choose Header", "Describe Header", "Define Type", "Done" };

		String[] titles = new String[] { "Upload CSV File", "Add Metadata",
				"Choose Header", "Describe Header", "Define Type", "Summary" };

		mProgressbarTracker = new ProgressbarTracker(6, labels);
		barPanel.add(mProgressbarTracker);

		FormWrapper wrapper1 = FormWrapper.getInstanceWithoutBackButton(
				getFormInStep1(), titles[0], 0);
		FormWrapper wrapper2 = FormWrapper.getInstance(getFormInStep2(),
				titles[1], 1);
		FormWrapper wrapper3 = FormWrapper.getInstance(getFormInStep3(),
				titles[2], 2);
		FormWrapper wrapper4 = FormWrapper.getInstance(getFormInStep4(),
				titles[3], 3);
		FormWrapper wrapper5 = FormWrapper.getInstance(getFormInStep5(),
				titles[4], 4);
		FormWrapper wrapper6 = FormWrapper.getInstanceWithoutNextButton(
				getFormInStep6(), titles[5], 5);

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

	private Widget getFormInStep1() {
		Label form1 = new Label();
		form1.setSize("400px", "200px");
		form1.setText("Form 1");
		return form1;
	}

	private Widget getFormInStep2() {
		Label form1 = new Label();
		form1.setSize("400px", "200px");
		form1.setText("Form 2");
		return form1;
	}

	private Widget getFormInStep3() {
		Label form1 = new Label();
		form1.setSize("400px", "200px");
		form1.setText("Form 3");
		return form1;
	}

	private Widget getFormInStep4() {
		Label form1 = new Label();
		form1.setSize("400px", "200px");
		form1.setText("Form 4");
		return form1;
	}

	private Widget getFormInStep5() {
		Label form1 = new Label();
		form1.setSize("400px", "200px");
		form1.setText("Form 5");
		return form1;
	}

	private Widget getFormInStep6() {
		Label form1 = new Label();
		form1.setSize("400px", "200px");
		form1.setText("Form 6");
		return form1;
	}

	public boolean isFromValidated(FormWrapper sender) {
		int index = sender.getIndexInParent();
		boolean isValidated = false;
		switch (index) {
		case 0:
			isValidated = true;
			break;
		case 1:
			isValidated = true;
			break;
		case 2:
			isValidated = true;
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
		mProgressbarTracker.setActiveIndex(index );
	}

}
