package org.cotrix.web.manage.client.codelists;

import org.cotrix.web.common.client.Presenter;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.manage.client.event.CodelistCreatedEvent;
import org.cotrix.web.manage.client.event.CreateNewVersionEvent;
import org.cotrix.web.manage.client.event.ManagerBus;
import org.cotrix.web.manage.client.event.OpenCodelistEvent;
import org.cotrix.web.manage.client.event.RefreshCodelistsEvent;
import org.cotrix.web.manage.client.event.RemoveCodelistEvent;
import org.cotrix.web.manage.client.event.RefreshCodelistsEvent.RefreshCodeListsHandler;
import org.cotrix.web.manage.shared.CodelistGroup;
import org.cotrix.web.manage.shared.CodelistGroup.Version;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistsPresenter implements Presenter, CodelistsView.Presenter {

	protected EventBus managerBus;
	
	protected CodelistsView view;
	
	@Inject
	protected CodelistsDataProvider codelistDataProvider;

	@Inject
	public CodelistsPresenter(@ManagerBus EventBus managerBus, CodelistsView view) {
		this.view = view;
		this.view.setPresenter(this);
		this.managerBus = managerBus;
		bind();
		featureBind();
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
	
	protected void featureBind()
	{
	/*	FeatureBinder.bind(new FeatureToggler() {
			
			@Override
			public void toggleFeature(boolean active) {
				view.setAddVersionVisible(active);
			}
		}, ApplicationFeatures.VERSIONING_CODELIST);
		
		FeatureBinder.bind(new FeatureToggler() {
			
			@Override
			public void toggleFeature(boolean active) {
				view.setRemoveCodelistVisible(active);
			}
		}, ApplicationFeatures.REMOVE_CODELIST);*/
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
