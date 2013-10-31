package org.cotrix.web.codelistmanager.client.manager;

import java.util.HashMap;
import java.util.Map;

import org.cotrix.web.codelistmanager.client.codelist.CodelistPanelPresenter;
import org.cotrix.web.codelistmanager.client.di.CodelistPanelFactory;
import org.cotrix.web.codelistmanager.client.event.ManagerBus;
import org.cotrix.web.codelistmanager.client.event.OpenCodelistEvent;
import org.cotrix.web.codelistmanager.client.event.OpenCodelistEvent.OpenCodeListHandler;
import org.cotrix.web.share.shared.codelist.UICodelist;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ContentPanelController implements OpenCodeListHandler {
	
	@Inject
	protected CodelistPanelFactory codeListPanelFactory;
	protected EventBus managerBus;
	protected ContentPanel view;
	protected Map<String, CodelistPanelPresenter> presenters = new HashMap<String, CodelistPanelPresenter>();
	
	@Inject
	public ContentPanelController(@ManagerBus EventBus managerBus, ContentPanel view) {
		this.managerBus = managerBus;
		this.view = view;
		managerBus.addHandler(OpenCodelistEvent.TYPE, this);
		checkTabVisibility();
	}

	@Override
	public void onOpenCodeList(OpenCodelistEvent event) {
		Log.trace("opening codelist "+event.getCodelist());
		final UICodelist codelist = event.getCodelist();
		
		CodelistPanelPresenter codeListPanelPresenter = codeListPanelFactory.build(codelist.getId());
		presenters.put(codelist.getId(), codeListPanelPresenter);
		Widget codelistPanel = codeListPanelPresenter.getView().asWidget();
		HasCloseHandlers<Widget> hasCloseHandlers = view.addCodeListPanel(codelistPanel, codelist.getName(), codelist.getVersion());
		hasCloseHandlers.addCloseHandler(new CloseHandler<Widget>() {

			@Override
			public void onClose(CloseEvent<Widget> event) {
				closeCodeList(codelist.getId());
			}
		});
		
		checkTabVisibility();
		
		view.setVisible(codelistPanel);
	}
	
	protected void closeCodeList(String id)
	{
		CodelistPanelPresenter codeListPanelPresenter = presenters.get(id);
		view.removeCodeListPanel(codeListPanelPresenter.getView().asWidget());
		presenters.remove(id);
		checkTabVisibility();
	}
	
	protected void checkTabVisibility()
	{
		if (presenters.size()>0) view.showCodelists();
		else view.showBlank();
	}
}
