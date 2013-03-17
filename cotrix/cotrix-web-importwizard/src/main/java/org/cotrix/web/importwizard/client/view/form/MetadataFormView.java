package org.cotrix.web.importwizard.client.view.form;

import org.cotrix.web.importwizard.client.presenter.MetadataFormPresenterImpl;
import org.cotrix.web.share.shared.Metadata;

import com.google.gwt.user.client.ui.Widget;

public interface MetadataFormView<T> {
	public interface Presenter<T> {
		void alert(String message);
	}
	void alert(String message);
	void setPresenter(MetadataFormPresenterImpl presenter);
	Metadata getMetadata();
	boolean isValidated();
	Widget asWidget();
}
