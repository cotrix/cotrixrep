package org.cotrix.web.manage.client.codelists;

import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.manage.client.codelists.CodelistTreeModel.Grouping;

import com.google.gwt.user.client.ui.UIObject;
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
		void onShowMenu();
		
		void onFilterQueryChange(String query);
	}
	
	public void reloadData();
	void setPresenter(Presenter presenter);
	Widget asWidget();
	UIObject getMenuTarget();
	
	void setAddCodelistVisible(boolean visible);
	void setRemoveCodelistVisible(boolean visible);
	void setVersionCodelistVisible(boolean visible);
	
	void groupBy(Grouping grouping);
	void refreshData();
}
