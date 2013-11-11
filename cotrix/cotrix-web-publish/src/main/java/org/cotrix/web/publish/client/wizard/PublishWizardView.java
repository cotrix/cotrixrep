package org.cotrix.web.publish.client.wizard;

import org.cotrix.web.share.client.wizard.WizardView;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface PublishWizardView extends WizardView {

	public enum PublishWizardButton implements WizardButton {
		NEXT, 
		BACK, 
		PUBLISH, 
		MANAGE, 
		NEW_PUBLISH;

		@Override
		public String getId() {
			return this.toString();
		}
	};
	
	Widget asWidget();
}
