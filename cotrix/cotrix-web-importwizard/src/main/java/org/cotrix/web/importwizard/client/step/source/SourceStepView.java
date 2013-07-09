package org.cotrix.web.importwizard.client.step.source;

import org.cotrix.web.share.shared.Metadata;

import com.google.gwt.user.client.ui.Widget;

public interface SourceStepView<T> {
	
	public interface Presenter<T> {
		void alert(String message);
	}
	
	void alert(String message);
	
	void setPresenter(SourceStepPresenterImpl presenter);
	
	Metadata getMetadata();
	
	boolean isValidated();
	
	Widget asWidget();
}
