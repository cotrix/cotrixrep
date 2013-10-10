package org.cotrix.web.codelistmanager.client.codelists;

import org.cotrix.web.share.shared.UICodelist;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodelistsView {
	
	public interface Presenter {
		void onCodelistItemSelected(UICodelist codelist);
		void onCodelistRemove();
		void onCodelistCreate();
		
	}
	
	public void refresh();
	void setPresenter(Presenter presenter);
	Widget asWidget();
}
