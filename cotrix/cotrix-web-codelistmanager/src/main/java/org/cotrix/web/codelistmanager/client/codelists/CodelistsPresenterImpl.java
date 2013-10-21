package org.cotrix.web.codelistmanager.client.codelists;

import org.cotrix.web.codelistmanager.client.event.ManagerBus;
import org.cotrix.web.codelistmanager.client.event.OpenCodelistEvent;
import org.cotrix.web.codelistmanager.client.event.RefreshCodelistsEvent;
import org.cotrix.web.codelistmanager.client.event.RefreshCodelistsEvent.RefreshCodeListsHandler;
import org.cotrix.web.codelistmanager.client.event.RemoveCodelistEvent;
import org.cotrix.web.share.shared.UICodelist;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistsPresenterImpl implements CodelistsPresenter, RefreshCodeListsHandler {

	protected EventBus managerBus;
	
	private CodelistsView view;


	@Inject
	public CodelistsPresenterImpl(@ManagerBus EventBus managerBus, CodelistsView view) {
		this.view = view;
		this.view.setPresenter(this);
		this.managerBus = managerBus;
		this.managerBus.addHandler(RefreshCodelistsEvent.TYPE, this);
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
		view.refresh();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void onCodelistItemSelected(UICodelist codelist) {
		managerBus.fireEvent(new OpenCodelistEvent(codelist));
	}

	@Override
	public void onRefreshCodeLists(RefreshCodelistsEvent event) {
		Log.trace("onRefreshCodeLists");
		view.refresh();		
	}

	@Override
	public void onCodelistRemove(UICodelist codelist) {
		managerBus.fireEvent(new RemoveCodelistEvent(codelist.getId()));
	}

	@Override
	public void onCodelistCreate() {
		// TODO to complete
		
	}

}
