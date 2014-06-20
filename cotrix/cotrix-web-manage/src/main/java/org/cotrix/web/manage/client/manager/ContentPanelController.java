package org.cotrix.web.manage.client.manager;

import java.util.HashMap;
import java.util.Map;

import org.cotrix.web.common.client.event.CodelistClosedEvent;
import org.cotrix.web.common.client.event.CodelistOpenedEvent;
import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.manage.client.codelist.CodelistPanelController;
import org.cotrix.web.manage.client.di.CodelistPanelFactory;
import org.cotrix.web.manage.client.event.CloseCodelistEvent;
import org.cotrix.web.manage.client.event.ManagerBus;
import org.cotrix.web.manage.client.event.OpenCodelistEvent;
import org.cotrix.web.manage.client.manager.ContentPanel.ContentPanelListener;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class ContentPanelController {
	
	interface ContentPanelControllerEventBinder extends EventBinder<ContentPanelController> {}
	
	
	@Inject
	protected CodelistPanelFactory codeListPanelFactory;
	
	@Inject @ManagerBus
	protected EventBus managerBus;
	protected ContentPanel view;
	protected Map<String, CodelistPanelController> presenters = new HashMap<String, CodelistPanelController>();
	
	@Inject
	@CotrixBus
	protected EventBus cotrixBus;
	
	@Inject
	public ContentPanelController(ContentPanel view) {
		this.view = view;
		checkTabVisibility();
	}
	
	@Inject
	private void bind(ContentPanelControllerEventBinder binder) {
		binder.bindEventHandlers(this, managerBus);
	}
	
	@Inject
	private void setupListening() {
		view.addListener(new ContentPanelListener() {
			
			@Override
			public void onCodelistTabSelected(Widget codelistPanel) {
				CodelistPanelController codelistController = getController(codelistPanel);
				codelistController.onSelected();
			}
		});
	}
	
	private CodelistPanelController getController(Widget codelistPanel) {
		for (CodelistPanelController controller:presenters.values()) {
			if (controller.getView() == codelistPanel) return controller;
		}
		return null;
	}

	@EventHandler
	void onOpenCodeList(OpenCodelistEvent event) {
		Log.trace("onOpenCodeList codelist "+event.getCodelist());
		UICodelist codelist = event.getCodelist();
		
		CodelistPanelController presenter = presenters.get(codelist.getId());
		
		if (presenter == null) {
			openCodelist(codelist);
		} else {
			view.setVisible(presenter.getView());
		}
	}
	
	@EventHandler
	void onCloseCodelist(CloseCodelistEvent event) {
		Log.trace("onCloseCodelist codelist "+event.getCodelist());
		UICodelist codelist = event.getCodelist();
		closeCodeList(codelist.getId());
	}
	
	protected void openCodelist(final UICodelist codelist)
	{
		Log.trace("openCodelist codelist "+codelist);
		CodelistPanelController codeListPanelPresenter = codeListPanelFactory.build(codelist);
		presenters.put(codelist.getId(), codeListPanelPresenter);
		Widget codelistPanel = codeListPanelPresenter.getView();
		HasCloseHandlers<Widget> hasCloseHandlers = view.addCodeListPanel(codelistPanel, ValueUtils.getLocalPart(codelist.getName()), codelist.getVersion());
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
		CodelistPanelController codeListPanelPresenter = presenters.get(id);
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
