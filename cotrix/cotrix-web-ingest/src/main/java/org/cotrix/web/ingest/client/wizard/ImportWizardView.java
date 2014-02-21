package org.cotrix.web.ingest.client.wizard;

import org.cotrix.web.wizard.client.WizardView;

import com.google.gwt.user.client.ui.Widget;

public interface ImportWizardView extends WizardView {

	public enum ImportWizardButton implements WizardButton {
		NEXT, 
		BACK, 
		IMPORT, 
		MANAGE, 
		NEW_IMPORT;

		@Override
		public String getId() {
			return this.toString();
		}
	};
	
	Widget asWidget();
}
