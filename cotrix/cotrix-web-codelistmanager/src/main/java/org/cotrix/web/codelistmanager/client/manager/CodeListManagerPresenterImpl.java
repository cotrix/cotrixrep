package org.cotrix.web.codelistmanager.client.manager;

import org.cotrix.web.codelistmanager.client.codelists.CodeListsPresenter;
import org.cotrix.web.codelistmanager.client.data.event.DataSavedEvent;
import org.cotrix.web.codelistmanager.client.data.event.DataSavedEvent.DataSavedHandler;
import org.cotrix.web.codelistmanager.client.data.event.SavingDataEvent;
import org.cotrix.web.codelistmanager.client.data.event.SavingDataEvent.SavingDataHandler;
import org.cotrix.web.codelistmanager.client.event.ManagerBus;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListManagerPresenterImpl implements CodeListManagerPresenter {

	protected EventBus managerBus;
	protected CodeListManagerView view;
	protected CodeListsPresenter codeListPresenter;
	protected ContentPanelController contentPanelController;
	
	@Inject
	public CodeListManagerPresenterImpl(@ManagerBus EventBus managerBus, CodeListManagerView view, CodeListsPresenter codeListPresenter, ContentPanelController contentPanelController){
		this.managerBus = managerBus;
		this.codeListPresenter = codeListPresenter;
		this.view = view;
		this.contentPanelController = contentPanelController;
		
		bind();
	}
	
	protected void bind()
	{
		managerBus.addHandler(SavingDataEvent.TYPE, new SavingDataHandler(){

			@Override
			public void onSavingData(SavingDataEvent event) {
				//TODO view.showAlert("Saving...");
			}
			
		});
		managerBus.addHandler(DataSavedEvent.TYPE, new DataSavedHandler() {
			
			@Override
			public void onDataSaved(DataSavedEvent event) {
				//TODO view.showAlert("Saving...");
			}
		});
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
		
		codeListPresenter.go(view.getWestPanel());
			
	}
}
