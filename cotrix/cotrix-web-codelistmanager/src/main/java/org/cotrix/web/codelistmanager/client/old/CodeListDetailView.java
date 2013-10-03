package org.cotrix.web.codelistmanager.client.old;

import org.cotrix.web.share.shared.CotrixImportModel;
import org.cotrix.web.share.shared.UICode;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodeListDetailView {
	public interface Presenter<T> {
		void onNavLeftClicked(boolean isShowingNavLeft);
		void onCodelistNameClicked(boolean isVisible);
		void onCellEdited(UICode value);
		void setDataProvider(DataGrid<UICode[]> ddtaGrid,String id);
		void insertRow(int row);
		void deleteRow(int row);
		void onSaveButtonClicked();
	}
	void setData(CotrixImportModel model,String id);
	void onCodelistNameClicked(ClickEvent event);
	void onNavLeftClicked(ClickEvent event);
	void onSaveButtonClicked(ClickEvent event);
	void showNavLeft();
	void enableSaveButton(boolean enable);
	void showNavRight();
	int getPageIndex();
	void showMetadataPanel(boolean isVisible);
	void setPresenter(Presenter presenter);
	void showActivityIndicator();
	Widget asWidget();
}