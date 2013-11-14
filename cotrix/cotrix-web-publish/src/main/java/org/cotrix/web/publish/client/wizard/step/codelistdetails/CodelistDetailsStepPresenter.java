package org.cotrix.web.publish.client.wizard.step.codelistdetails;

import org.cotrix.web.share.client.wizard.step.VisualWizardStep;
import org.cotrix.web.share.shared.codelist.UICodelistMetadata;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodelistDetailsStepPresenter extends VisualWizardStep {

	public void setCodelist(UICodelistMetadata codelistMetadata);
}
