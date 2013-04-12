package org.cotrix.web.publish.client.view;

import java.util.ArrayList;

import org.cotrix.web.share.shared.Codelist;

import com.google.gwt.user.client.ui.Widget;

public interface CodeListView{
	public interface Presenter<T> {
		void onCodelistItemClicked(int id);
	}
	void init(ArrayList<Codelist> codelists);
	void setPresenter(Presenter presenter);
	Widget asWidget();
}
