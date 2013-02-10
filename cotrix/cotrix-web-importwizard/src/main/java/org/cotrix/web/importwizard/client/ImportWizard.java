package org.cotrix.web.importwizard.client;

import org.cotrix.web.importwizard.client.progressbar.ProgressbarTracker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class ImportWizard extends Composite {

	private static ImportWizardUiBinder uiBinder = GWT
			.create(ImportWizardUiBinder.class);

	interface ImportWizardUiBinder extends UiBinder<Widget, ImportWizard> {
	}

	@UiField
	FlowPanel panel;

	public ImportWizard() {
		initWidget(uiBinder.createAndBindUi(this));
		String[] labels = new String[] {    "Upload File", 
											"Add Metadata",
											"Choose Header",
											"Describe Header",
											"Define Type",
											"Done" };
		ProgressbarTracker mProgressbarTracker = new ProgressbarTracker(6,
				labels);
		mProgressbarTracker.setActiveIndex(3);
		panel.add(mProgressbarTracker);
	}

	public ImportWizard(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
