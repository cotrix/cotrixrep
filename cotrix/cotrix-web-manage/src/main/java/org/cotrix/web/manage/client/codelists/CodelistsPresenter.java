package org.cotrix.web.manage.client.codelists;

import org.cotrix.web.common.client.Presenter;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureToggler;
import org.cotrix.web.common.client.feature.InstanceFeatureBind;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.feature.ApplicationFeatures;
import org.cotrix.web.manage.client.codelist.event.RemoveCodelistEvent;
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
	
	private UICodelist lastSelected;

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
	private void featureBind()
	{
		FeatureBinder.bind(new FeatureToggler() {
			
			@Override
			public void toggleFeature(boolean active) {
				view.setAddCodelistVisible(active);
			}
		}, ApplicationFeatures.CREATE_CODELIST);
	/*	FeatureBinder.bind(new FeatureToggler() {
			
			@Override
			public void toggleFeature(boolean active) {
				view.setAddVersionVisible(active);
			}
		}, ApplicationFeatures.VERSIONING_CODELIST);*/
		
		FeatureBinder.bind(new FeatureToggler() {
			
			@Override
			public void toggleFeature(boolean active) {
				view.setRemoveCodelistVisible(active);
			}
		}, new InstanceFeatureBind.IdProvider() {
			
			@Override
			public String getId() {
				return lastSelected!=null?lastSelected.getId():null;
			}
		}, ApplicationFeatures.REMOVE_CODELIST);
	}
	
	@Inject
	protected void setupDialog() {
		newCodelistDialog.setListener(new NewCodelistDialogListener() {
			
			@Override
			public void onCreate(String name, String version) {
				managerBus.fireEvent(new CreateNewCodelistEvent(name, version));
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
		lastSelected = codelist;
		if (codelist!=null) managerBus.fireEvent(new OpenCodelistEvent(codelist));
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
}
