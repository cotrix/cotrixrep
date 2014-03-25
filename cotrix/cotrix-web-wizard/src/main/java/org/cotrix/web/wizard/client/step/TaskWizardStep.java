/**
 * 
 */
package org.cotrix.web.wizard.client.step;

import org.cotrix.web.wizard.client.WizardAction;

import com.google.gwt.core.client.Callback;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface TaskWizardStep extends WizardStep {
	
	public boolean isComplete();
	public WizardAction getAction();
	
	public void run(TaskCallBack callback);
	
	public interface TaskCallBack extends Callback<WizardAction, Throwable>{}

}
