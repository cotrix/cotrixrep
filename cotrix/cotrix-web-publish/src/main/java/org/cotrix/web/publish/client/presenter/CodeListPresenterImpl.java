package org.cotrix.web.publish.client.presenter;

import java.util.ArrayList;

import org.cotrix.web.publish.client.PublishServiceAsync;
import org.cotrix.web.publish.client.view.CodeListView;
import org.cotrix.web.share.shared.Codelist;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class CodeListPresenterImpl implements CodeListPresenter {
	private PublishServiceAsync rpcService;
	private HandlerManager eventBus;
	private CodeListView view;

	public interface OnCodelistItemClicked{
		void onCodelistItemClicked(int id);
	}
	private OnCodelistItemClicked onCodelistItemClicked;
	
	@Inject
	public CodeListPresenterImpl(PublishServiceAsync rpcService,HandlerManager eventBus, CodeListView view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
		rpcService.getAllCodelists(new AsyncCallback<ArrayList<Codelist>>() {
			public void onSuccess(ArrayList<Codelist> result) {
				view.init(result);
			}
			
			public void onFailure(Throwable caught) {
				
			}
		});

	}

	public void setOnCodelistItemClicked(OnCodelistItemClicked onCodelistItemClicked){
		this.onCodelistItemClicked = onCodelistItemClicked;
	}
	
	public void onCodelistItemClicked(int id) {
		onCodelistItemClicked.onCodelistItemClicked(id);
	}

	public void refresh() {
		rpcService.getAllCodelists(new AsyncCallback<ArrayList<Codelist>>() {
			public void onSuccess(ArrayList<Codelist> result) {
				view.init(result);
			}
			
			public void onFailure(Throwable caught) {
				Window.alert("error ++"+caught.getMessage());
			}
		});
	}


}
