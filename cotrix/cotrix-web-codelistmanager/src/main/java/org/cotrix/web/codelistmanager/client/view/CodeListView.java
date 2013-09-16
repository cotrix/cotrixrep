package org.cotrix.web.codelistmanager.client.view;

import java.util.ArrayList;

import org.cotrix.web.share.shared.UICodelist;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodeListView {
	public interface Presenter {
		void onCodelistItemClicked(String id);
	}
	void init(ArrayList<UICodelist> codelists);
	void setPresenter(Presenter presenter);
	Widget asWidget();
}
