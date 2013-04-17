package org.cotrix.web.publish.client.presenter;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.publish.client.CotrixPublishAppGinInjector;
import org.cotrix.web.publish.client.PublishServiceAsync;
import org.cotrix.web.publish.client.view.ChanelPropertyItem;
import org.cotrix.web.publish.client.view.CodeListDetailView;
import org.cotrix.web.share.shared.CotrixImportModel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
		init();
	}
	
	private void  init(){
		final CotrixPublishAppGinInjector injector = GWT.create(CotrixPublishAppGinInjector.class);

		final ChanelPropertyItem item1 = new ChanelPropertyItem("SDMX Registry");
		final ChanelPropertyItem item2 = new ChanelPropertyItem("Semantic Turkey (SKOS)");
		final ChanelPropertyItem item3 = new ChanelPropertyItem("FIGIS RTMS");
		final ChanelPropertyItem item4 = new ChanelPropertyItem("FLOD");
		item1.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(item1.isChecked()){
					ChanelPropertyPresenter presenter =  injector.getChanelPropertyPresenter();
					presenter.show();
				}
			}
		});
		item2.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(item2.isChecked()){
					ChanelPropertyPresenter presenter =  injector.getChanelPropertyPresenter();
					presenter.show();
				}
			}
		});
		item3.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(item3.isChecked()){
					ChanelPropertyPresenter presenter =  injector.getChanelPropertyPresenter();
					presenter.show();
				}
			}
		});
		item4.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(item4.isChecked()){
					ChanelPropertyPresenter presenter =  injector.getChanelPropertyPresenter();
					presenter.show();
				}
			}
		});
		
		view.addChanelItem(item1);
		view.addChanelItem(item2);
		view.addChanelItem(item3);
		view.addChanelItem(item4);
		
	}

	public void setData(final int id) {
		view.showActivityIndicator();
		rpcService.getCodeListModel(id, new AsyncCallback<CotrixImportModel>() {
			public void onSuccess(CotrixImportModel result) {
				view.setData(result,id);
			}

			public void onFailure(Throwable caught) {
				Window.alert("Can not get codelist data");
			}
		});

	}

	

}
