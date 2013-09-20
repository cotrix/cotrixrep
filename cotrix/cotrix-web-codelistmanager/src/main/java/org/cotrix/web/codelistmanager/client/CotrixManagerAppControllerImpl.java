package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.client.presenter.CodeListManagerPresenter;
import org.cotrix.web.share.client.event.CodeListImportedEvent;
import org.cotrix.web.share.client.event.CotrixBus;

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
		
	}

}
