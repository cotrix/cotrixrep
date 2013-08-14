package org.cotrix.web.importwizard.client.step.repositorydetails;

import org.cotrix.web.importwizard.client.step.WizardStep;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface RepositoryDetailsStepPresenter extends WizardStep, RepositoryDetailsStepView.Presenter {

	public void setRepository(String repositoryId);
}
