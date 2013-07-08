package org.cotrix.web.importwizard.client.step.preview;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 * @param <T>
 */
public interface PreviewStepView<T> {
	public interface Presenter<T> {
		void onCheckBoxChecked(boolean isChecked);
	}
	void onChecked(ClickEvent event);
	void showHeaderForm(boolean show);
	ArrayList<String> getHeaders();
	void alert(String message);
	void setData(String[] headers, ArrayList<String[]> data);
	void setPresenter(PreviewStepPresenterImpl presenter);
	Widget asWidget();
}
