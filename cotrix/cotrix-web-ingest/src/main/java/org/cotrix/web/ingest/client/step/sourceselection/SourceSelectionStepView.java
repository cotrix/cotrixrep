package org.cotrix.web.ingest.client.step.sourceselection;

import com.google.gwt.user.client.ui.Widget;

public interface SourceSelectionStepView {
	
	public interface Presenter {
		void onCloudButtonClick();
		void onLocalButtonClick();
	}
	
	void alert(String message);
	void setPresenter(Presenter presenter);
	
	Widget asWidget();
}
