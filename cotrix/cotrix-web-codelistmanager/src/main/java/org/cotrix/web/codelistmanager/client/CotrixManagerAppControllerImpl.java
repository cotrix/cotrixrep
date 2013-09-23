package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.client.event.ManagerBus;
import org.cotrix.web.codelistmanager.client.event.RefreshCodeListsEvent;
import org.cotrix.web.codelistmanager.client.presenter.CodeListManagerPresenter;
import org.cotrix.web.share.client.CotrixModule;
import org.cotrix.web.share.client.event.CodeListImportedEvent;
import org.cotrix.web.share.client.event.CotrixBus;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixManagerAppControllerImpl implements CotrixManagerAppController {
	
	protected EventBus cotrixBus;
	
	@Inject
	@ManagerBus
	protected EventBus managerBus;
	
	private ManagerServiceAsync rpcService;
	private HandlerManager eventBus;
	private CodeListManagerPresenter codeListManagerPresenter;
	
	@Inject
	public CotrixManagerAppControllerImpl(@CotrixBus EventBus cotrixBus, ManagerServiceAsync rpcService,HandlerManager eventBus,CodeListManagerPresenter codeListManagerPresenter) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.codeListManagerPresenter = codeListManagerPresenter;
		this.cotrixBus = cotrixBus;
		this.cotrixBus.addHandler(CodeListImportedEvent.TYPE, new CodeListImportedEvent.CodeListImportedHandler() {
			
			@Override
			public void onCodeListImported(CodeListImportedEvent event) {
				refreshCodeLists();
			}
		});
	}
	
	public void go(HasWidgets container) {
		this.codeListManagerPresenter.go(container);
	}
	
	public void onValueChange(ValueChangeEvent<String> event) {
	}

	public void refreshCodeLists() {
		Log.trace("refreshCodeLists");
		managerBus.fireEvent(new RefreshCodeListsEvent());
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
