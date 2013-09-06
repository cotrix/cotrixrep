package org.cotrix.web.importwizard.client;

import java.util.List;

import org.cotrix.web.importwizard.client.progresstracker.ProgressTracker.ProgressStep;
import org.cotrix.web.importwizard.client.step.WizardStep;

import com.google.gwt.user.client.ui.Widget;

public interface ImportWizardView {
	
	public enum WizardButton {NEXT, BACK, IMPORT, MANAGE, NEW_IMPORT};
	
	void setPresenter(Presenter presenter);

	public interface Presenter {
		
		void onButtonClicked(WizardButton button);
	}
	
	void addStep(WizardStep step);
	public void setLabels(List<ProgressStep> steps);
	public void setStepTitle(String title);
	public void setStepSubtitle(String subtitle);
	public void showStep(WizardStep step);
	public void showLabel(ProgressStep step);
	
	public void showButton(WizardButton button);
	public void hideButton(WizardButton button);
	
	public void hideAllButtons();
	
	Widget asWidget();

}
