package org.cotrix.web.manage.client.codelists;

import java.util.Set;

import org.cotrix.web.common.client.Presenter;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureToggler;
import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.feature.ApplicationFeatures;
import org.cotrix.web.common.shared.feature.UIFeature;
import org.cotrix.web.manage.client.codelist.event.CreateNewVersionEvent;
import org.cotrix.web.manage.client.codelist.event.RemoveCodelistEvent;
import org.cotrix.web.manage.client.codelist.metadata.VersionDialog;
import org.cotrix.web.manage.client.codelist.metadata.VersionDialog.VersionDialogListener;
import org.cotrix.web.manage.client.codelists.CodelistsFeatureCache.CodelistsFeatureCacheListener;
import org.cotrix.web.manage.client.codelists.NewCodelistDialog.NewCodelistDialogListener;
import org.cotrix.web.manage.client.event.CodelistCreatedEvent;
import org.cotrix.web.manage.client.event.CodelistRemovedEvent;
import org.cotrix.web.manage.client.event.CreateNewCodelistEvent;
import org.cotrix.web.manage.client.event.ManagerBus;
import org.cotrix.web.manage.client.event.OpenCodelistEvent;
import org.cotrix.web.manage.client.event.RefreshCodelistsEvent;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistsPresenter implements Presenter, CodelistsView.Presenter {
	
	interface CodelistsPresenterEventBinder extends EventBinder<CodelistsPresenter> {}

	@Inject @ManagerBus
	private EventBus managerBus;
	
	private CodelistsView view;
	
	@Inject
	private CodelistsDataProvider codelistDataProvider;
	
	@Inject
	private NewCodelistDialog newCodelistDialog;
	
	@Inject
	private VersionDialog versionDialog;
	
	private UICodelist lastSelected;
	
	@Inject
	private CodelistsFeatureCache featureCache;

	@Inject
	public CodelistsPresenter(CodelistsView view) {
		this.view = view;
		this.view.setPresenter(this);
	}
	
	@Inject
	private void bind(CodelistsPresenterEventBinder binder) {
		binder.bindEventHandlers(this, managerBus);
	}
	
	@Inject
	private void featureBind(FeatureBinder featureBinder)
	{
		featureBinder.bind(new FeatureToggler() {
			
			@Override
			public void toggleFeature(boolean active) {
				view.setAddCodelistVisible(active);
			}
		}, ApplicationFeatures.CREATE_CODELIST);
		
		featureCache.addListener(new CodelistsFeatureCacheListener() {
			
			@Override
			public void onFeatureUpdate(String resourceId, Set<UIFeature> features) {
				if (lastSelected!=null && lastSelected.getId().equals(resourceId)) refreshButtonsState();
			}
		});
		
		refreshButtonsState();
	}
	
	@Inject
	protected void setupDialogs() {
		newCodelistDialog.setListener(new NewCodelistDialogListener() {
			
			@Override
			public void onCreate(String name, String version) {
				managerBus.fireEvent(new CreateNewCodelistEvent(name, version));
			}
		});
		versionDialog.setListener(new VersionDialogListener() {
			
			@Override
			public void onCreate(String id, String newVersion) {
				managerBus.fireEvent(new CreateNewVersionEvent(id, newVersion));
			}
		});
	}
	
	@EventHandler
	void onCodelistCreated(CodelistCreatedEvent event) {
		codelistDataProvider.addCodelistGroup(event.getCodelistGroup());
	}
	
	@EventHandler
	void onRefreshCodelists(RefreshCodelistsEvent event) {
		refreshCodeLists();
	}
	
	@EventHandler
	void onCodelistRemoved(CodelistRemovedEvent event) {
		codelistDataProvider.removeCodelistGroup(event.getCodelistGroup());
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
		view.refresh();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void onCodelistItemSelected(UICodelist codelist) {
		Log.trace("onCodelistItemSelected codelist: "+codelist);
		lastSelected = codelist;
		if (codelist!=null) managerBus.fireEvent(new OpenCodelistEvent(codelist));
		refreshButtonsState();
	}
	
	private void refreshButtonsState() {
		if (lastSelected!=null) {
			boolean canRemove = featureCache.hasFeature(lastSelected.getId(), ApplicationFeatures.REMOVE_CODELIST);
			view.setRemoveCodelistVisible(canRemove);
			boolean canVersion = featureCache.hasFeature(lastSelected.getId(), ApplicationFeatures.VERSION_CODELIST);
			view.setVersionCodelistVisible(canVersion);
		} else {
			view.setRemoveCodelistVisible(false);
			view.setVersionCodelistVisible(false);
		}
	}

	public void refreshCodeLists() {
		Log.trace("onRefreshCodeLists");
		view.refresh();		
	}

	@Override
	public void onCodelistRemove(UICodelist codelist) {
		Log.trace("onCodelistRemove codelist: "+codelist);
		managerBus.fireEvent(new RemoveCodelistEvent(codelist));
	}

	@Override
	public void onCodelistCreate() {
		newCodelistDialog.showCentered();
	}

	
	@Override
	public void onCodelistNewVersion(UICodelist codelist) {
		versionDialog.setOldVersion(codelist.getId(), ValueUtils.getLocalPart(codelist.getName()), codelist.getVersion());
		versionDialog.showCentered();
	}
}
