package org.cotrix.web.ingest.client.step.codelistdetails;

import org.cotrix.web.ingest.shared.AssetInfo;
import org.cotrix.web.wizard.client.step.VisualWizardStep;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodelistDetailsStepPresenter extends VisualWizardStep, CodelistDetailsStepView.Presenter {

	public void setAsset(AssetInfo asset);
}
