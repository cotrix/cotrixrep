package org.cotrix.web.codelistmanager.client.presenter;

import java.util.ArrayList;

import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.client.view.CodeListView;
import org.cotrix.web.share.shared.UICodelist;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class CodeListPresenterImpl implements CodeListPresenter {
	private ManagerServiceAsync rpcService;
	private HandlerManager eventBus;
	private CodeListView view;

	public interface OnCodelistItemClicked{
		void onCodelistItemClicked(String id);
	}
	private OnCodelistItemClicked onCodelistItemClicked;

	@Inject
	public CodeListPresenterImpl(ManagerServiceAsync rpcService,HandlerManager eventBus, CodeListView view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
		rpcService.getAllCodelists(new AsyncCallback<ArrayList<UICodelist>>() {
			public void onSuccess(ArrayList<UICodelist> result) {
				view.init(result);
			}
			
			public void onFailure(Throwable caught) {
				Log.error("Error loading the codelists", caught);
			}
		});

	}

	public void setOnCodelistItemClicked(OnCodelistItemClicked onCodelistItemClicked){
		this.onCodelistItemClicked = onCodelistItemClicked;
	}
	
	public void onCodelistItemClicked(String id) {
		onCodelistItemClicked.onCodelistItemClicked(id);
	}

	public void refresh() {
		rpcService.getAllCodelists(new AsyncCallback<ArrayList<UICodelist>>() {
			public void onSuccess(ArrayList<UICodelist> result) {
				view.init(result);
			}
			public void onFailure(Throwable caught) {
				Window.alert("Refreshing codelist error.");
			}
		});
	}


}
