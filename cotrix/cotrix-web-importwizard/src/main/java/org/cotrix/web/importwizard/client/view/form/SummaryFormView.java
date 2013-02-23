package org.cotrix.web.importwizard.client.view.form;

import com.google.gwt.user.client.ui.Widget;

public interface SummaryFormView<T> {
	public interface Presenter<T> {
		
	}
	void initForm(String[] headers);
	Widget asWidget();
}
