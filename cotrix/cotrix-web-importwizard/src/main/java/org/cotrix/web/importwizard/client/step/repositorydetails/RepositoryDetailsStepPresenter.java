package org.cotrix.web.importwizard.client.step.repositorydetails;

import org.cotrix.web.importwizard.client.step.VisualWizardStep;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface RepositoryDetailsStepPresenter extends VisualWizardStep, RepositoryDetailsStepView.Presenter {

	public void setRepository(String repositoryId);
}
