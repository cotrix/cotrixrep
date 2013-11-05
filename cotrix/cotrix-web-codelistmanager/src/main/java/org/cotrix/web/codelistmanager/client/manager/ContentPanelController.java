package org.cotrix.web.codelistmanager.client.manager;

import java.util.HashMap;
import java.util.Map;

import org.cotrix.web.codelistmanager.client.codelist.CodelistPanelPresenter;
import org.cotrix.web.codelistmanager.client.codelist.CodelistPanelView;
import org.cotrix.web.codelistmanager.client.di.CodelistPanelFactory;
import org.cotrix.web.codelistmanager.client.event.ManagerBus;
import org.cotrix.web.codelistmanager.client.event.OpenCodelistEvent;
import org.cotrix.web.codelistmanager.client.event.OpenCodelistEvent.OpenCodeListHandler;
import org.cotrix.web.share.client.event.CodelistClosedEvent;
import org.cotrix.web.share.client.event.CodelistOpenedEvent;
import org.cotrix.web.share.client.event.CotrixBus;
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
	@CotrixBus
	protected EventBus cotrixBus;
	
	@Inject
	public ContentPanelController(@ManagerBus EventBus managerBus, ContentPanel view) {
		this.managerBus = managerBus;
		this.view = view;
		managerBus.addHandler(OpenCodelistEvent.TYPE, this);
		checkTabVisibility();
	}

	@Override
	public void onOpenCodeList(OpenCodelistEvent event) {
		Log.trace("onOpenCodeList codelist "+event.getCodelist());
		UICodelist codelist = event.getCodelist();
		
		CodelistPanelPresenter presenter = presenters.get(codelist.getId());
		
		if (presenter == null) {
			openCodelist(codelist);
		} else {
			view.setVisible(presenter.getView());
		}
		
	}
	
	protected void openCodelist(final UICodelist codelist)
	{
		Log.trace("openCodelist codelist "+codelist);
		CodelistPanelPresenter codeListPanelPresenter = codeListPanelFactory.build(codelist.getId());
		presenters.put(codelist.getId(), codeListPanelPresenter);
		CodelistPanelView codelistPanel = codeListPanelPresenter.getView();
		HasCloseHandlers<Widget> hasCloseHandlers = view.addCodeListPanel(codelistPanel, codelist.getName(), codelist.getVersion());
		hasCloseHandlers.addCloseHandler(new CloseHandler<Widget>() {

			@Override
			public void onClose(CloseEvent<Widget> event) {
				closeCodeList(codelist.getId());
			}
		});
		
		checkTabVisibility();
		
		view.setVisible(codelistPanel);
		cotrixBus.fireEvent(new CodelistOpenedEvent(codelist.getId()));
	}
	
	protected void closeCodeList(String id)
	{
		CodelistPanelPresenter codeListPanelPresenter = presenters.get(id);
		view.removeCodeListPanel(codeListPanelPresenter.getView());
		presenters.remove(id);
		checkTabVisibility();
		cotrixBus.fireEvent(new CodelistClosedEvent(id));
	}
	
	protected void checkTabVisibility()
	{
		if (presenters.size()>0) view.showCodelists();
		else view.showBlank();
	}
}
