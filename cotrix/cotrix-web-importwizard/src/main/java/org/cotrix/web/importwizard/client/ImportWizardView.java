package org.cotrix.web.importwizard.client;

import java.util.List;

import org.cotrix.web.importwizard.client.step.WizardStep;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public interface ImportWizardView {
	void setPresenter(Presenter presenter);

	public interface Presenter {
		void onFowardButtonClicked();
		void onBackwardButtonClicked();

		void onUploadOtherButtonClicked();
		void onManageCodelistButtonClicked();
		void addForm(HasWidgets container);
	}
	
	void addStep(WizardStep step);
	public void setLabels(List<WizardStep> steps);
	public void setStepTitle(String title);
	public void showStep(WizardStep step);
	
	public void hideBackwardButton();
	public void showBackwardButton();
	public void setBackwardButtonLabel(String label);
	
	public void hideForwardButton();
	public void showForwardButton();
	public void setForwardButtonLabel(String label);
	
	Widget asWidget();

}
