package org.cotrix.web.share.client.wizard;

import java.util.List;

import org.cotrix.web.share.client.wizard.progresstracker.ProgressTracker.ProgressStep;
import org.cotrix.web.share.client.wizard.step.VisualWizardStep;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface WizardView {
	
	public interface WizardButton {
		String getId();		
	};
	
	void setPresenter(Presenter presenter);

	public interface Presenter {
		
		void onButtonClicked(WizardButton button);
	}
	
	void addStep(VisualWizardStep step);
	public void setLabels(List<ProgressStep> steps);
	public void setStepTitle(String title);
	public void setStepSubtitle(String subtitle);
	public void showStep(VisualWizardStep step);
	public void showLabel(ProgressStep step);
	
	public void showButton(WizardButton button);
	public void hideButton(WizardButton button);
	
	public void hideAllButtons();
	
	void showProgress();
	void hideProgress();

}