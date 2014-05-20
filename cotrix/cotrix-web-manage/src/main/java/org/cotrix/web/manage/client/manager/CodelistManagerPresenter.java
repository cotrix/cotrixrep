package org.cotrix.web.manage.client.manager;

import org.cotrix.web.common.client.Presenter;
import org.cotrix.web.manage.client.codelists.CodelistsPresenter;
import org.cotrix.web.manage.client.data.event.DataSavedEvent;
import org.cotrix.web.manage.client.data.event.SavingDataEvent;
import org.cotrix.web.manage.client.data.event.DataSavedEvent.DataSavedHandler;
import org.cotrix.web.manage.client.data.event.SavingDataEvent.SavingDataHandler;
import org.cotrix.web.manage.client.event.ManagerBus;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistManagerPresenter implements Presenter {

	protected EventBus managerBus;
	protected CodelistManagerView view;
	protected CodelistsPresenter codeListPresenter;
	protected ContentPanelController contentPanelController;
	
	@Inject
	public CodelistManagerPresenter(@ManagerBus EventBus managerBus, CodelistManagerView view, CodelistsPresenter codeListPresenter, ContentPanelController contentPanelController){
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
