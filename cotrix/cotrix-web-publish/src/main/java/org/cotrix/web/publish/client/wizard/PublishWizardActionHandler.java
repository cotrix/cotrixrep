/**
 * 
 */
package org.cotrix.web.publish.client.wizard;

import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.wizard.client.WizardAction;
import org.cotrix.web.wizard.client.WizardActionHandler;
import org.cotrix.web.wizard.client.WizardController;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class PublishWizardActionHandler implements WizardActionHandler {

	@Inject
	@PublishBus
	protected EventBus publishBus;

	@Override
	public boolean handle(WizardAction action, WizardController controller) {
		if (action instanceof PublishWizardAction) {
			PublishWizardAction importWizardAction = (PublishWizardAction)action;
			switch (importWizardAction) {
				case NEW_PUBLISH: publishBus.fireEvent(new ResetWizardEvent()); break;

			}
		}
		return false;
	}

}
