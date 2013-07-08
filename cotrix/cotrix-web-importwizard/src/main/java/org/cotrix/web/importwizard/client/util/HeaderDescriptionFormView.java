package org.cotrix.web.importwizard.client.util;

import java.util.HashMap;

import com.google.gwt.user.client.ui.Widget;

public interface HeaderDescriptionFormView<T> {
	public interface Presenter<T> {
		
	}
	void initForm(String[] headers);
	void alert(String message);
	void setPresenter(HeaderDescriptionPresenterImpl presenter);
	HashMap<String, String> getHeaderDescription();
	Widget asWidget();
	
}
