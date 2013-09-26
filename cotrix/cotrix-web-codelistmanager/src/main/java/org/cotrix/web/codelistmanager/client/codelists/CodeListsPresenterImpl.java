package org.cotrix.web.codelistmanager.client.codelists;

import org.cotrix.web.codelistmanager.client.event.ManagerBus;
import org.cotrix.web.codelistmanager.client.event.OpenCodeListEvent;
import org.cotrix.web.codelistmanager.client.event.RefreshCodeListsEvent;
import org.cotrix.web.codelistmanager.client.event.RefreshCodeListsEvent.RefreshCodeListsHandler;
import org.cotrix.web.share.shared.UICodelist;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListsPresenterImpl implements CodeListsPresenter, RefreshCodeListsHandler {

	protected EventBus managerBus;
	
	private CodeListsView view;


	@Inject
	public CodeListsPresenterImpl(@ManagerBus EventBus managerBus, CodeListsView view) {
		this.view = view;
		this.view.setPresenter(this);
		this.managerBus = managerBus;
		this.managerBus.addHandler(RefreshCodeListsEvent.TYPE, this);
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
		view.refresh();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void onCodelistItemSelected(UICodelist codelist) {
		managerBus.fireEvent(new OpenCodeListEvent(codelist));
	}

	@Override
	public void onRefreshCodeLists(RefreshCodeListsEvent event) {
		Log.trace("onRefreshCodeLists");
		view.refresh();		
	}

}
