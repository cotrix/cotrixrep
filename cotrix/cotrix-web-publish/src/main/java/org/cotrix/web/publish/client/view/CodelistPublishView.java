package org.cotrix.web.publish.client.view;

import com.google.gwt.user.client.ui.Widget;

public interface CodelistPublishView {
	public interface Presenter<T> {

	}

	void setPresenter(Presenter presenter);
	Widget asWidget();

}
