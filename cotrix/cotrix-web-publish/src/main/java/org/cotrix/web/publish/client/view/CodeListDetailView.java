package org.cotrix.web.publish.client.view;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.share.shared.UICodelist;
import org.cotrix.web.share.shared.CotrixImportModel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Widget;

public interface CodeListDetailView {
	public interface Presenter<T> {
		void onNavLeftClicked(boolean isShowingNavLeft);
		void onPublishButtonClicked();
	}
	void setData(CotrixImportModel model,String id);
	void showActivityIndicator();
	void addChanelItem(ChanelPropertyItem item);
	void addPublishButton();
	void addTitle(String title,String Description);
	void onNavLeftClicked(ClickEvent event);
	void showNavLeft();
	void showNavRight();
	ArrayList<String> getUserSelectedChanels();
	void setPresenter(Presenter presenter);
	Widget asWidget();
}
