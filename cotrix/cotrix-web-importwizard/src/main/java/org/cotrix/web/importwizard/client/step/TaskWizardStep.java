/**
 * 
 */
package org.cotrix.web.importwizard.client.step;

import org.cotrix.web.importwizard.client.wizard.WizardAction;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface TaskWizardStep extends WizardStep {
	
	public void run(AsyncCallback<WizardAction> callback);

}
