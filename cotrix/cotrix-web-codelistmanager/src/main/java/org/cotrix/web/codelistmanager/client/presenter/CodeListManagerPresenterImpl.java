package org.cotrix.web.codelistmanager.client.presenter;

import org.cotrix.web.codelistmanager.client.event.ManagerBus;
import org.cotrix.web.codelistmanager.client.view.CodeListManagerView;

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
	protected CodeListPresenter codeListPresenter;
	protected ContentPanelController contentPanelController;
	
	@Inject
	public CodeListManagerPresenterImpl(@ManagerBus EventBus managerBus, CodeListManagerView view, CodeListPresenter codeListPresenter, ContentPanelController contentPanelController){
		this.managerBus = managerBus;
		this.codeListPresenter = codeListPresenter;
		this.view = view;
		this.contentPanelController = contentPanelController;
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
		
		codeListPresenter.go(view.getWestPanel());
			
	}
}
