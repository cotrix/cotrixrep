package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.client.event.CodelistCreatedEvent;
import org.cotrix.web.codelistmanager.client.event.CreateNewVersionEvent;
import org.cotrix.web.codelistmanager.client.event.CreateNewVersionEvent.CreateNewVersionHandler;
import org.cotrix.web.codelistmanager.client.event.ManagerBus;
import org.cotrix.web.codelistmanager.client.event.OpenCodelistEvent;
import org.cotrix.web.codelistmanager.client.event.RefreshCodelistsEvent;
import org.cotrix.web.codelistmanager.client.manager.CodelistManagerPresenter;
import org.cotrix.web.codelistmanager.client.resources.CotrixManagerResources;
import org.cotrix.web.codelistmanager.shared.CodelistGroup;
import org.cotrix.web.share.client.CotrixModule;
import org.cotrix.web.share.client.error.ManagedFailureCallback;
import org.cotrix.web.share.client.event.CodeListImportedEvent;
import org.cotrix.web.share.client.event.CotrixBus;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixManagerAppControllerImpl implements CotrixManagerAppController {
	
	@Inject
	protected ManagerServiceAsync service;
	
	protected EventBus cotrixBus;
	protected EventBus managerBus;
	
	private CodelistManagerPresenter codeListManagerPresenter;
	
	@Inject
	public CotrixManagerAppControllerImpl(@CotrixBus EventBus cotrixBus, @ManagerBus EventBus managerBus, CodelistManagerPresenter codeListManagerPresenter) {
		this.codeListManagerPresenter = codeListManagerPresenter;
		this.cotrixBus = cotrixBus;
		this.managerBus = managerBus;
		
		CotrixManagerResources.INSTANCE.css().ensureInjected();
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
		service.createNewCodelistVersion(codelistId, newVersion, new ManagedFailureCallback<CodelistGroup>() {
			
			@Override
			public void onSuccess(CodelistGroup result) {
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
