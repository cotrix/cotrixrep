package org.cotrix.web.manage.client.codelists;

import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.manage.shared.CodelistGroup.Version;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodelistsView {
	
	public interface Presenter {
		void onCodelistItemSelected(UICodelist codelist);
		void onCodelistRemove(UICodelist codelist);
		void onCodelistCreate(Version version);
		void onCodelistNewVersion(String id, String newVersion);
	}
	
	public void refresh();
	void setPresenter(Presenter presenter);
	Widget asWidget();
	void showVersionDialog(Version oldVersion);
	void setAddVersionVisible(boolean visible);
	void setRemoveCodelistVisible(boolean visible);
}
