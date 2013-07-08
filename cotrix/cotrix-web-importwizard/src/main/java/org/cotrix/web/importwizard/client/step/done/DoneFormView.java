package org.cotrix.web.importwizard.client.step.done;

import org.cotrix.web.importwizard.client.step.preview.HeaderSelectionFormPresenterImpl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public interface DoneFormView {
	public interface Presenter {
	}
	void setPresenter(Presenter presenter);
	void setDoneTitle(String title);
	void setWarningMessage(String message);
	Widget asWidget();
}
