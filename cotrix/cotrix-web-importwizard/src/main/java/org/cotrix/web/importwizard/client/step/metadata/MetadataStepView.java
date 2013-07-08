package org.cotrix.web.importwizard.client.step.metadata;

import org.cotrix.web.share.shared.Metadata;

import com.google.gwt.user.client.ui.Widget;

public interface MetadataStepView<T> {
	public interface Presenter<T> {
		void alert(String message);
	}
	void alert(String message);
	void setPresenter(MetadataStepPresenterImpl presenter);
	Metadata getMetadata();
	boolean isValidated();
	Widget asWidget();
}
