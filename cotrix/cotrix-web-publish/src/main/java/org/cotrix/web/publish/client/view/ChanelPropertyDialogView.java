package org.cotrix.web.publish.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;

public interface ChanelPropertyDialogView {
	public interface Presenter<T> {
		void onDoneButtonClicked(String url,String username,String password);
	}
	void setPresenter(Presenter presenter);
	void onDoneButtonClicked(ClickEvent event);
	void hide();
	void show();
	Widget asWidget();

}
