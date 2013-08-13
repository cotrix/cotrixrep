package org.cotrix.web.importwizard.client;

import java.util.List;

import org.cotrix.web.importwizard.client.progresstracker.ProgressTracker.ProgressStep;
import org.cotrix.web.importwizard.client.step.WizardStep;

import com.google.gwt.user.client.ui.Widget;

public interface ImportWizardView {
	void setPresenter(Presenter presenter);

	public interface Presenter {
		void onFowardButtonClicked();
		void onBackwardButtonClicked();
	}
	
	void addStep(WizardStep step);
	public void setLabels(List<ProgressStep> steps);
	public void setStepTitle(String title);
	public void setStepSubtitle(String subtitle);
	public void showStep(WizardStep step);
	public void showLabel(ProgressStep step);
	
	public void hideBackwardButton();
	public void showBackwardButton();
	public void setBackwardButton(String label, String style);
	
	public void hideForwardButton();
	public void showForwardButton();
	public void setForwardButton(String label, String style);
	
	Widget asWidget();

}
