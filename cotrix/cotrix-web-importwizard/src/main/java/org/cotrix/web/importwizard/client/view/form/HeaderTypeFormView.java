package org.cotrix.web.importwizard.client.view.form;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;

public interface HeaderTypeFormView<T> {
	public interface Presenter<T> {

	}
	void initForm(String[] headers);
	Widget asWidget();

}
