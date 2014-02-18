package org.cotrix.web.share.client.wizard;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DefaultWizardActionHandler implements WizardActionHandler {

	@Override
	public boolean handle(WizardAction action, WizardController controller) {
		if (action == WizardAction.NONE) return true;
		
		if (action == WizardAction.BACK) {
			controller.goBack();
			return true;
		}
		
		if (action == WizardAction.NEXT) {
			controller.goForward();
			return true;
		}
		return false;
	}
	
}