/**
 * 
 */
package org.cotrix.web.wizard.client;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface WizardAction {
	
	public static final WizardAction NONE = new WizardAction() {
	};	
	public static final WizardAction BACK = new WizardAction() {
	};
	public static final WizardAction NEXT = new WizardAction() {
	};

}
