package org.cotrix.web.importwizard.client.step.metadata;

import org.cotrix.web.importwizard.shared.ImportMetadata;

import com.google.gwt.user.client.ui.Widget;

public interface MetadataStepView {
	
	public interface Presenter {

	}

	ImportMetadata getMetadata();
	void setMetadata(ImportMetadata metadata);
	
	void alert(String message);

	Widget asWidget();
}
