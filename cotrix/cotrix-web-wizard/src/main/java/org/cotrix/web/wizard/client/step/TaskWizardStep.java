/**
 * 
 */
package org.cotrix.web.wizard.client.step;

import org.cotrix.web.wizard.client.WizardAction;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface TaskWizardStep extends WizardStep {
	
	public boolean isComplete();
	public WizardAction getAction();
	
	public void run(AsyncCallback<WizardAction> callback);

}
