package org.cotrix.web.importwizard.client.step.codelistdetails;

import org.cotrix.web.importwizard.shared.AssetInfo;
import org.cotrix.web.wizard.client.step.VisualWizardStep;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodelistDetailsStepPresenter extends VisualWizardStep, CodelistDetailsStepView.Presenter {

	public void setAsset(AssetInfo asset);
}
