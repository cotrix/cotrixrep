/**
 * 
 */
package org.cotrix.web.publish.client.wizard;

import org.cotrix.web.publish.client.wizard.PublishWizardView.PublishWizardButton;
import org.cotrix.web.wizard.client.step.StepButton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PublishWizardStepButtons {

	public static final StepButton BACKWARD = new StepButton(PublishWizardAction.BACK, PublishWizardButton.BACK);
	public static final StepButton FORWARD = new StepButton(PublishWizardAction.NEXT, PublishWizardButton.NEXT);
	public static final StepButton PUBLISH = new StepButton(PublishWizardAction.NEXT, PublishWizardButton.PUBLISH);
	public static final StepButton NEW_PUBLISH = new StepButton(PublishWizardAction.NEW_PUBLISH, PublishWizardButton.NEW_PUBLISH);
	public static final StepButton DOWNLOAD_REPORT = new StepButton(PublishWizardAction.DOWNLOAD_REPORT, PublishWizardButton.DOWNLOAD_REPORT);
	public static final StepButton DOWNLOAD_CODELIST = new StepButton(PublishWizardAction.DOWNLOAD_CODELIST, PublishWizardButton.DOWNLOAD_CODELIST);
}
