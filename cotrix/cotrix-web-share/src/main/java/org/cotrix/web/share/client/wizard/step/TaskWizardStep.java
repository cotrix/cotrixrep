/**
 * 
 */
package org.cotrix.web.share.client.wizard.step;

import org.cotrix.web.share.client.wizard.WizardAction;

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
