package org.cotrix.web.importwizard.client.step.codelistdetails;

import org.cotrix.web.importwizard.client.step.WizardStep;
import org.cotrix.web.importwizard.shared.AssetInfo;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodelistDetailsStepPresenter extends WizardStep, CodelistDetailsStepView.Presenter {

	public void setAsset(AssetInfo asset);
}
