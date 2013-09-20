package org.cotrix.web.codelistmanager.client.view;

import org.cotrix.web.share.shared.UICodelist;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodeListView {
	
	public interface Presenter {
		void onCodelistItemSelected(UICodelist id);
	}
	
	public void refresh();
	void setPresenter(Presenter presenter);
	Widget asWidget();
}
