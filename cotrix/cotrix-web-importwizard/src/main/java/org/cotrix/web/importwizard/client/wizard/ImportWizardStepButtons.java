/**
 * 
 */
package org.cotrix.web.importwizard.client.wizard;

import org.cotrix.web.importwizard.client.wizard.ImportWizardView.ImportWizardButton;
import org.cotrix.web.share.client.wizard.step.StepButton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImportWizardStepButtons {

	public static final StepButton BACKWARD = new StepButton(ImportWizardAction.BACK, ImportWizardButton.BACK);
	public static final StepButton FORWARD = new StepButton(ImportWizardAction.NEXT, ImportWizardButton.NEXT);
	public static final StepButton IMPORT = new StepButton(ImportWizardAction.NEXT, ImportWizardButton.IMPORT);
	public static final StepButton NEW_IMPORT = new StepButton(ImportWizardAction.NEW_IMPORT, ImportWizardButton.NEW_IMPORT);
	public static final StepButton MANAGE = new StepButton(ImportWizardAction.MANAGE, ImportWizardButton.MANAGE);

}
