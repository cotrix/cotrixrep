package org.cotrix.web.importwizard.client.step.summary;

import java.util.ArrayList;
import java.util.HashMap;

import org.cotrix.web.share.shared.HeaderType;
import org.cotrix.web.share.shared.Metadata;
import org.cotrix.web.share.shared.Metadata;

import com.google.gwt.user.client.ui.Widget;

public interface SummaryFormView<T> {
	
	public interface Presenter<T> {
		
	}
	void setHeader(String[] headers);
	void alert(String message);
	void setDescription(HashMap<String, String> description);
	void setHeaderType(HashMap<String,HeaderType> headerType);
	void setMetadata(Metadata metadata);
	void setPresenter(SummaryFormPresenterImpl summaryFormPresenter);

	Widget asWidget();
}
