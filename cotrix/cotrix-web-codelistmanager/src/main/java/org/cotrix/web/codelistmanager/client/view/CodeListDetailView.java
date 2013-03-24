package org.cotrix.web.codelistmanager.client.view;

import java.util.ArrayList;

import org.cotrix.web.share.shared.Codelist;
import org.cotrix.web.share.shared.CotrixImportModel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;

public interface CodeListDetailView {
	public interface Presenter<T> {
		void onNavLeftClicked(boolean isShowingNavLeft);
		void onCodelistNameClicked(boolean isVisible);
	}
	void setData(CotrixImportModel model);
	void onNavLeftClicked(ClickEvent event);
	void onCodelistNameClicked(ClickEvent event);
	void showNavLeft();
	void showNavRight();
	void showMetadataPanel(boolean isVisible);
	void expandWidth(boolean isExpand);
	void setPresenter(Presenter presenter);
	Widget asWidget();
}
