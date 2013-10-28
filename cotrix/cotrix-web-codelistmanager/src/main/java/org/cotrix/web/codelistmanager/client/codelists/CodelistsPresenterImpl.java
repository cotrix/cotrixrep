package org.cotrix.web.codelistmanager.client.codelists;

import org.cotrix.web.codelistmanager.client.event.CodelistCreatedEvent;
import org.cotrix.web.codelistmanager.client.event.CreateNewVersionEvent;
import org.cotrix.web.codelistmanager.client.event.ManagerBus;
import org.cotrix.web.codelistmanager.client.event.OpenCodelistEvent;
import org.cotrix.web.codelistmanager.client.event.RefreshCodelistsEvent;
import org.cotrix.web.codelistmanager.client.event.RefreshCodelistsEvent.RefreshCodeListsHandler;
import org.cotrix.web.codelistmanager.client.event.RemoveCodelistEvent;
import org.cotrix.web.codelistmanager.shared.CodelistGroup;
import org.cotrix.web.codelistmanager.shared.CodelistGroup.Version;
import org.cotrix.web.share.shared.UICodelist;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistsPresenterImpl implements CodelistsPresenter {

	protected EventBus managerBus;
	
	protected CodelistsView view;
	
	@Inject
	protected CodelistDataProvider codelistDataProvider;

	@Inject
	public CodelistsPresenterImpl(@ManagerBus EventBus managerBus, CodelistsView view) {
		this.view = view;
		this.view.setPresenter(this);
		this.managerBus = managerBus;
		bind();
	}
	
	protected void bind()
	{
		managerBus.addHandler(RefreshCodelistsEvent.TYPE, new RefreshCodeListsHandler() {
			
			@Override
			public void onRefreshCodeLists(RefreshCodelistsEvent event) {
				refreshCodeLists();
			}
		});
		managerBus.addHandler(CodelistCreatedEvent.TYPE, new CodelistCreatedEvent.CodelistCreatedHandler() {
			
			@Override
			public void onCodelistCreated(CodelistCreatedEvent event) {
				newCodelist(event.getCodelistGroup());
			}
		});
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

	public void refreshCodeLists() {
		Log.trace("onRefreshCodeLists");
		view.refresh();		
	}
	
	public void newCodelist(CodelistGroup newGroup)
	{
		Log.trace("newCodelist newGroup: "+newGroup);
		CodelistGroup oldGroup = null;
		Log.trace("dataprovide: "+codelistDataProvider);
		Log.trace("cache: "+codelistDataProvider.getCache());
		for (CodelistGroup group:codelistDataProvider.getCache()) {
			if (group.equals(newGroup)) {
				oldGroup = group;
				break;
			}
		}
		
		Log.trace("oldGroup: "+oldGroup);
		
		if (oldGroup!=null) oldGroup.addVersions(newGroup.getVersions());
		else codelistDataProvider.getCache().add(newGroup);
		
		Log.trace("refreshing cache: "+codelistDataProvider.getCache());
		codelistDataProvider.refresh();
	}

	@Override
	public void onCodelistRemove(UICodelist codelist) {
		managerBus.fireEvent(new RemoveCodelistEvent(codelist.getId()));
	}

	@Override
	public void onCodelistCreate(Version version) {
		if (version!=null) {
			view.showVersionDialog(version);
		}
	}

	@Override
	public void onCodelistNewVersion(String id, String newVersion) {
		managerBus.fireEvent(new CreateNewVersionEvent(id, newVersion));
	}

}
