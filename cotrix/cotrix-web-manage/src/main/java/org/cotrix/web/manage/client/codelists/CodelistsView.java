package org.cotrix.web.manage.client.codelists;

import org.cotrix.web.common.shared.codelist.UICodelist;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(CodelistsViewImpl.class)
public interface CodelistsView {
	
	public interface Presenter {
		void onCodelistItemSelected(UICodelist codelist);
		void onCodelistRemove(UICodelist codelist);
		void onCodelistNewVersion(UICodelist codelist);
		void onCodelistCreate();
	}
	
	public void refresh();
	void setPresenter(Presenter presenter);
	Widget asWidget();
	
	void setAddCodelistVisible(boolean visible);
	void setRemoveCodelistVisible(boolean visible);
	void setVersionCodelistVisible(boolean visible);
}
