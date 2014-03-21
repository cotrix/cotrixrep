package org.cotrix.web.manage.client;

import org.cotrix.web.common.client.CotrixModule;
import org.cotrix.web.common.client.CotrixModuleController;
import org.cotrix.web.common.client.Presenter;
import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.event.CodeListImportedEvent;
import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.event.CodelistCreatedEvent;
import org.cotrix.web.manage.client.event.CreateNewVersionEvent;
import org.cotrix.web.manage.client.event.ManagerBus;
import org.cotrix.web.manage.client.event.OpenCodelistEvent;
import org.cotrix.web.manage.client.event.RefreshCodelistsEvent;
import org.cotrix.web.manage.client.event.CreateNewVersionEvent.CreateNewVersionHandler;
import org.cotrix.web.manage.client.manager.CodelistManagerPresenter;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.shared.CodelistGroup;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CotrixManageController implements Presenter, ValueChangeHandler<String>, CotrixModuleController {
	
	@Inject
	protected ManageServiceAsync service;
	
	protected EventBus cotrixBus;
	protected EventBus managerBus;
	
	private CodelistManagerPresenter codeListManagerPresenter;
	
	@Inject
	public CotrixManageController(@CotrixBus EventBus cotrixBus, @ManagerBus EventBus managerBus, CodelistManagerPresenter codeListManagerPresenter) {
		this.codeListManagerPresenter = codeListManagerPresenter;
		this.cotrixBus = cotrixBus;
		this.managerBus = managerBus;
		
		this.cotrixBus.addHandler(CodeListImportedEvent.TYPE, new CodeListImportedEvent.CodeListImportedHandler() {
			
			@Override
			public void onCodeListImported(CodeListImportedEvent event) {
				refreshCodeLists();
			}
		});
		this.managerBus.addHandler(CreateNewVersionEvent.TYPE, new CreateNewVersionHandler(){

			@Override
			public void onCreateNewVersion(CreateNewVersionEvent event) {
				createNewVersion(event.getCodelistId(), event.getNewVersion());
			}});
	}
	
	@Inject
	private void setupCss(CotrixManagerResources resources) {
		resources.css().ensureInjected();
	}
	
	public void go(HasWidgets container) {
		this.codeListManagerPresenter.go(container);
	}
	
	public void onValueChange(ValueChangeEvent<String> event) {
	}

	public void refreshCodeLists() {
		Log.trace("refreshCodeLists");
		managerBus.fireEvent(new RefreshCodelistsEvent());
	}
	
	public void createNewVersion(String codelistId, String newVersion)
	{
		Log.trace("createNewVersion codelistId: "+codelistId+" newVersion: "+newVersion);
		service.createNewCodelistVersion(codelistId, newVersion, new ManagedFailureCallback<CodelistGroup>() {
			
			@Override
			public void onSuccess(CodelistGroup result) {
				Log.trace("created "+result);
				managerBus.fireEvent(new OpenCodelistEvent(result.getVersions().get(0).toUICodelist()));
				managerBus.fireEvent(new CodelistCreatedEvent(result));
			}
		});
	}

	@Override
	public CotrixModule getModule() {
		return CotrixModule.MANAGE;
	}

	@Override
	public void activate() {
		refreshCodeLists();	
	}

	@Override
	public void deactivate() {
	}

}
