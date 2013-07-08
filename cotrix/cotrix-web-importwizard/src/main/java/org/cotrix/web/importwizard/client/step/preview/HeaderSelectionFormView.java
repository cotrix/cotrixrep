package org.cotrix.web.importwizard.client.step.preview;

import java.util.ArrayList;

import org.cotrix.web.importwizard.client.step.summary.SummaryFormPresenterImpl;
import org.cotrix.web.importwizard.client.step.upload.UploadFormPresenterImpl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public interface HeaderSelectionFormView<T> {
	public interface Presenter<T> {
		void onCheckBoxChecked(boolean isChecked);
	}
	void onChecked(ClickEvent event);
	void showHeaderForm(boolean show);
	ArrayList<String> getHeaders();
	void alert(String message);
	void setData(String[] headers, ArrayList<String[]> data);
	void setPresenter(HeaderSelectionFormPresenterImpl presenter);
	Widget asWidget();
}
