package org.cotrix.web.codelistmanager.client.view;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.share.shared.Codelist;
import org.cotrix.web.share.shared.CotrixImportModel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Widget;

public interface CodeListDetailView {
	public interface Presenter<T> {
		void onNavLeftClicked(boolean isShowingNavLeft);
		void onCodelistNameClicked(boolean isVisible);
		void setDataProvider(DataGrid<String[]> ddtaGrid,int id);
	}
	void setData(CotrixImportModel model,int id);
	void onNavLeftClicked(ClickEvent event);
	void onCodelistNameClicked(ClickEvent event);
	void showNavLeft();
	void showNavRight();
	void showMetadataPanel(boolean isVisible);
	void setPresenter(Presenter presenter);
	void showActivityIndicator();
	Widget asWidget();
}
