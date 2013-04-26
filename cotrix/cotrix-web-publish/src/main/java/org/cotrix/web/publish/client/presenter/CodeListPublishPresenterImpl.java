package org.cotrix.web.publish.client.presenter;

import org.cotrix.web.publish.client.PublishServiceAsync;
import org.cotrix.web.publish.client.view.CodeListPublishView;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class CodeListPublishPresenterImpl implements CodeListPublishPresenter{
	private PublishServiceAsync rpcService;
	private HandlerManager eventBus;
	private CodeListPublishView view;
	private CodeListPresenter codeListPresenter;
	private CodeListDetailPresenter codeListDetailPresenter;
	
	@Inject
	public CodeListPublishPresenterImpl(PublishServiceAsync rpcService,HandlerManager eventBus,CodeListPresenter codeListPresenter,CodeListDetailPresenter codeListDetailPresenter, CodeListPublishView view){
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.codeListPresenter = codeListPresenter;
		this.codeListDetailPresenter = codeListDetailPresenter;
		this.view = view;
		this.view.setPresenter(this);
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
		codeListPresenter.go(view.getContentPanel());
		codeListPresenter.setOnCodelistItemClicked(this);
		codeListDetailPresenter.go(view.getContentPanel());
		codeListDetailPresenter.setOnNavigationLeftClicked(CodeListPublishPresenterImpl.this);
	}

	public void onNavigationClicked(boolean isShowingNavLeft) {
		view.showLeftPanel(isShowingNavLeft);
	}
	public void onCodelistItemClicked(int id) {
		codeListDetailPresenter.setData(id);
	}

	public void refresh() {
		codeListPresenter.refresh();
	}
	
}
