package org.cotrix.web.codelistmanager.client.manager;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.codelistmanager.client.CodeListPanelFactory;
import org.cotrix.web.codelistmanager.client.CotrixManagerAppGinInjector;
import org.cotrix.web.codelistmanager.client.codelist.CodeListPanelPresenter;
import org.cotrix.web.codelistmanager.client.event.ManagerBus;
import org.cotrix.web.codelistmanager.client.event.OpenCodeListEvent;
import org.cotrix.web.codelistmanager.client.event.OpenCodeListEvent.OpenCodeListHandler;
import org.cotrix.web.share.shared.UICodelist;

import com.allen_sauer.gwt.log.client.Log;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ContentPanelController implements OpenCodeListHandler {

	
	@Inject
	protected CodeListPanelFactory codeListPanelFactory;
	protected EventBus managerBus;
	protected ContentPanel view;
	protected List<CodeListPanelPresenter> presenters = new ArrayList<CodeListPanelPresenter>();
	

	@Inject
	public ContentPanelController(@ManagerBus EventBus managerBus, ContentPanel view) {
		this.managerBus = managerBus;
		this.view = view;
		managerBus.addHandler(OpenCodeListEvent.TYPE, this);
		checkTabVisibility();
	}


	@Override
	public void onOpenCodeList(OpenCodeListEvent event) {
		Log.trace("opening codelist "+event.getCodelist());
		UICodelist codelist = event.getCodelist();
		

		CodeListPanelPresenter codeListPanelPresenter = codeListPanelFactory.build(codelist.getId());
		presenters.add(codeListPanelPresenter);
		//codeListPanelPresenter.setCodeList(codelist);

		
		view.addCodeListPanel(codeListPanelPresenter.getView().asWidget(), codelist.getName());
		checkTabVisibility();
	}
	
	protected void checkTabVisibility()
	{
		if (presenters.size()>0) view.showCodelists();
		else view.showBlank();
	}


}
