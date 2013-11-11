package org.cotrix.web.publish.client.wizard.step.typeselection;

import com.google.gwt.user.client.ui.Widget;

public interface TypeSelectionStepView {
	
	public interface Presenter {
		void onSDMXButtonClick();
		void onCSVButtonClick();
	}
	
	void alert(String message);
	void setPresenter(Presenter presenter);
	
	Widget asWidget();
}
