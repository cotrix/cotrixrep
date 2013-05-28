package org.cotrix.web.publish.client.view;

import java.util.ArrayList;

import org.cotrix.web.share.shared.UICodelist;

import com.google.gwt.user.client.ui.Widget;

public interface CodeListView{
	public interface Presenter<T> {
		void onCodelistItemClicked(String id);
	}
	void init(ArrayList<UICodelist> codelists);
	void setPresenter(Presenter presenter);
	Widget asWidget();
}