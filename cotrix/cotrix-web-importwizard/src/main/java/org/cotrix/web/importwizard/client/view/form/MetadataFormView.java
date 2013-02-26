package org.cotrix.web.importwizard.client.view.form;

import org.cotrix.web.importwizard.client.presenter.MetadataFormPresenterImpl;
import org.cotrix.web.importwizard.client.presenter.SummaryFormPresenterImpl;

import com.google.gwt.user.client.ui.Widget;

public interface MetadataFormView<T> {
	public interface Presenter<T> {
	
	}
	void setPresenter(MetadataFormPresenterImpl presenter);
	Widget asWidget();
}
