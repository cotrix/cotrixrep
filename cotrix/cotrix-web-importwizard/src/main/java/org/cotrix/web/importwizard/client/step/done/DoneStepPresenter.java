package org.cotrix.web.importwizard.client.step.done;

import org.cotrix.web.importwizard.client.step.WizardStep;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface DoneStepPresenter extends WizardStep, DoneStepView.Presenter {
	
	public void setDoneTitle(String title);
	public void setWarningMessage(String message);
}
