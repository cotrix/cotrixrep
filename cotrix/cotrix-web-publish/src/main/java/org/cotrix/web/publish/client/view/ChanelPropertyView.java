package org.cotrix.web.publish.client.view;

import com.google.gwt.user.client.ui.Widget;

public interface ChanelPropertyView {
	public interface Presenter<T> {
		void show();
		
	}
	void setPresenter(Presenter presenter);
	void show();
	Widget asWidget();

}
