package org.cotrix.web.importwizard.client.step.done;

import org.cotrix.web.importwizard.client.Presenter;
import org.cotrix.web.importwizard.client.step.WizardStep;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface DoneStepPresenter extends Presenter<DoneStepPresenterImpl>, DoneStepView.Presenter, WizardStep {
	
	public void setDoneTitle(String title);
	public void setWarningMessage(String message);
}
