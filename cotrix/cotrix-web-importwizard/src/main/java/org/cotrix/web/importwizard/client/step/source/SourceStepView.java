package org.cotrix.web.importwizard.client.step.source;

import com.google.gwt.user.client.ui.Widget;

public interface SourceStepView {
	
	public interface Presenter {
		void onCloudButtonClick();
		void onLocalButtonClick();
	}
	
	void alert(String message);
	void setPresenter(Presenter presenter);
	
	Widget asWidget();
}
