package org.cotrix.web.publish.client.wizard.step.destinationselection;

import com.google.gwt.user.client.ui.Widget;

public interface DestinationSelectionStepView {
	
	public interface Presenter {
		void onCloudButtonClick();
		void onLocalButtonClick();
	}
	
	void alert(String message);
	void setPresenter(Presenter presenter);
	
	Widget asWidget();
}
