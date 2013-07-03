package org.cotrix.web.publish.client.presenter;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.publish.client.PublishServiceAsync;
import org.cotrix.web.publish.client.view.ChanelPropertyItem;
import org.cotrix.web.publish.client.view.CodeListDetailView;
import org.cotrix.web.publish.client.view.ProgressbarDialog;
import org.cotrix.web.share.shared.CotrixImportModel;
import org.cotrix.web.share.shared.UIChanel;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.inject.Inject;

public class CodeListDetailPresenterImpl implements CodeListDetailPresenter {
	private PublishServiceAsync rpcService;
	private HandlerManager eventBus;
	private CodeListDetailView view;
	private AsyncDataProvider<String[]> dataProvider;
	private List<String[]> currentData;
	private String codelistID;
	public interface OnNavigationClicked {
		public void onNavigationClicked(boolean isShowingNavLeft);
	}

	private OnNavigationClicked onNavigationClicked;
	
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

	public void onNavLeftClicked(boolean isShowingNavLeft) {
		if (isShowingNavLeft) {
			onNavigationClicked.onNavigationClicked(true);
			view.showNavRight();
		} else {
			onNavigationClicked.onNavigationClicked(false);
			view.showNavLeft();
		}
	}

	public void setOnNavigationLeftClicked(
			OnNavigationClicked onNavigationClicked) {
		this.onNavigationClicked = onNavigationClicked;
	}

	private void  init(){
		String description = "​<div width=\"100%\" style=\"text-align:center;\"><h2>What is a Publication Channel?</h2></div>"+
				"<p>A publication channel is the connection between Cotrix and an publication endpoint. Cotrix will publish a codelist in the selected version in a certain format IN the publication endpoint.</p>"+
				"<p>Often the format of the publication endpoint is inherent by the publication endpoint itself. For instance SDMX for a SDMX Registry. For Triple Stores it could be plain RDF or SKOS, this is yet not supported by Cotrix.</p>"+
				"<p>An publication endpoint can be a SDMX Registry, a RDBMS database like Oracle or Postgres, a Triple store, etc.</p>"+
				"<p>An publication endpoint is usually external to Cotrix. It could be that Cotrix will provide \"local\" publication\" endpoints for CSV or MS-Excell versions of the codelists, this is yet not supported.</p>";

		view.addTitle("Publication Channels",description);
		rpcService.getAllChanels(new AsyncCallback<ArrayList<UIChanel>>() {
			public void onFailure(Throwable arg0) {
				Window.alert("Getting chanel fail");
			}

			public void onSuccess(ArrayList<UIChanel> uiChanels) {
				for (UIChanel uiChanel : uiChanels) {
					ChanelPropertyItem item = new ChanelPropertyItem(uiChanel);
					item.setUIPropertyType(uiChanel.getAssetTypes());
					item.addProperties(uiChanel.getProperties());
					view.addChanelItem(item);
					
				}
				view.addPublishButton();
			}
		});


	}

	public void setData(final String id) {
		this.codelistID = id;
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
	public void onPublishButtonClicked() {

		final ProgressbarDialog d = new ProgressbarDialog();
		d.show();
		
		
		
		ArrayList<String> chanels = view.getUserSelectedChanels(); 
		rpcService.publishCodelist(codelistID, chanels, new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				d.hide();
				Window.alert("Publish Codelist fail");
				
			}

			public void onSuccess(Void result) {
				d.hide();
				Window.alert("Publish Codelist Success");
			}
		});
	}



}
