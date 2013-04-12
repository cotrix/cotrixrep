package org.cotrix.web.publish.client.presenter;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.publish.client.PublishServiceAsync;
import org.cotrix.web.publish.client.view.CodeListDetailView;
import org.cotrix.web.share.shared.CotrixImportModel;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;

public class CodeListDetailPresenterImpl implements CodeListDetailPresenter {
	public interface OnNavigationClicked {
		public void onNavigationClicked(boolean isShowingNavLeft);
	}

	private OnNavigationClicked onNavigationClicked;

	private PublishServiceAsync rpcService;
	private HandlerManager eventBus;
	private CodeListDetailView view;
	private AsyncDataProvider<String[]> dataProvider;
	private List<String[]> currentData;

	@Inject
	public CodeListDetailPresenterImpl(PublishServiceAsync rpcService,
			HandlerManager eventBus, CodeListDetailView view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
	}

	public void go(HasWidgets container) {
		HorizontalPanel hp = (HorizontalPanel) container;
		hp.add(view.asWidget());
		hp.setCellWidth(hp.getWidget(1), "100%");
	}



	public void onCodelistNameClicked(boolean isVisible) {
		view.showMetadataPanel(isVisible);
	}

	public void setData(final int id) {
		view.showActivityIndicator();
	}

	

}
