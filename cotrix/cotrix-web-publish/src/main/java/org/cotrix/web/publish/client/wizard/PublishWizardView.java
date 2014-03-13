package org.cotrix.web.publish.client.wizard;

import org.cotrix.web.wizard.client.WizardView;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(PublishWizardViewImpl.class)
public interface PublishWizardView extends WizardView {

	public enum PublishWizardButton implements WizardButton {
		NEXT, 
		BACK, 
		PUBLISH, 
		NEW_PUBLISH;

		@Override
		public String getId() {
			return this.toString();
		}
	};
	
	Widget asWidget();
}
