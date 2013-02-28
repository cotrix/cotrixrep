package org.cotrix.web.importwizard.client.view.form;

import java.util.HashMap;

import org.cotrix.web.importwizard.client.presenter.SummaryFormPresenter;
import org.cotrix.web.importwizard.client.presenter.SummaryFormPresenterImpl;
import org.cotrix.web.importwizard.client.presenter.UploadFormPresenterImpl;
import org.cotrix.web.importwizard.shared.HeaderType;
import org.cotrix.web.importwizard.shared.Metadata;

import com.google.gwt.user.client.ui.Widget;

public interface SummaryFormView<T> {
	
	public interface Presenter<T> {
		
	}
	void setHeader(String[] headers);
	void alert(String message);
	void setDescription(HashMap<String, String> description);
	void setHeaderType(HashMap<String, HeaderType> headerType);
	void setMetadata(Metadata metadata);
	void setPresenter(SummaryFormPresenterImpl summaryFormPresenter);

	Widget asWidget();
}
