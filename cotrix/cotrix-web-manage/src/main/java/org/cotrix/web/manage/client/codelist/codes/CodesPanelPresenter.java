package org.cotrix.web.manage.client.codelist.codes;

import org.cotrix.web.common.client.Presenter;
import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.feature.AsyncCallBackWrapper;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.codelist.NewStateEvent;
import org.cotrix.web.manage.client.codelist.SwitchPanelEvent;
import org.cotrix.web.manage.client.codelist.codes.CodesToolbar.Action;
import org.cotrix.web.manage.client.codelist.codes.CodesToolbar.ToolBarListener;
import org.cotrix.web.manage.client.data.AttributeTypeBridge;
import org.cotrix.web.manage.client.data.CodeAttributeBridge;
import org.cotrix.web.manage.client.data.CodeBridge;
import org.cotrix.web.manage.client.data.CodeLinkBridge;
import org.cotrix.web.manage.client.data.DataSaverManager;
import org.cotrix.web.manage.client.data.LinkTypeBridge;
import org.cotrix.web.manage.client.data.MetadataAttributeBridge;
import org.cotrix.web.manage.client.data.MetadataBridge;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.shared.ManagerUIFeature;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodesPanelPresenter implements Presenter {
	
	interface CodesPanelPresenterEventBinder extends EventBinder<CodesPanelPresenter> {}

	private CodesPanelView view;
	private String codelistId;
	private ManageServiceAsync service;
	
	@Inject
	private DataSaverManager saverManager;
	
	@Inject @CurrentCodelist
	private UICodelist codelist;

	@Inject @CodelistBus 
	private EventBus codelistBus;

	@Inject
	public CodesPanelPresenter(CodesPanelView view, @CurrentCodelist String codelistId, ManageServiceAsync service, DataSaverManager saverManager) {
		this.view = view;
		this.codelistId = codelistId;
		this.service = service;
		this.saverManager = saverManager;
		
		bind();
		bindFeatures();
		
		showAllGroupsAsColumn();
		loadState();
	}
	
	
	@Inject
	private void bindSavers(
			CodeBridge codeModifyCommandGenerator,
			CodeAttributeBridge codeAttributeCommandGenerator,
			MetadataBridge metadataModifyCommandGenerator,
			MetadataAttributeBridge metadataAttributeModifyGenerator,
			LinkTypeBridge linkTypeModifyGenerator,
			CodeLinkBridge codeLinkCommandGenerator,
			AttributeTypeBridge attributeTypeModifyGenerator
			) {
		saverManager.register(codeModifyCommandGenerator);
		saverManager.register(codeAttributeCommandGenerator);
		saverManager.register(metadataModifyCommandGenerator);
		saverManager.register(metadataAttributeModifyGenerator);
		saverManager.register(linkTypeModifyGenerator);
		saverManager.register(codeLinkCommandGenerator);
		saverManager.register(attributeTypeModifyGenerator);
	}
	
	@Inject
	protected void bind(@CodelistBus EventBus bus, CodesPanelPresenterEventBinder eventBinder) {
		eventBinder.bindEventHandlers(this, bus);
	}
	
	private void bind()
	{
		// TOOLBAR
		CodesToolbar toolbar = view.getToolBar();
		
		toolbar.setListener(new ToolBarListener() {
			
			@Override
			public void onAction(Action action) {
				Log.trace("toolbar onAction "+action);
				switch (action) {
					case ALL_COLUMN: showAllGroupsAsColumn(); break;
					case ALL_NORMAL: view.getCodeListEditor().showAllGroupsAsNormal(); break;
					case TO_METADATA: codelistBus.fireEvent(SwitchPanelEvent.METADATA); break;
				}
			}
		});
	}
	
	private void loadState() {
		view.getToolBar().showStateLoader(true);
		service.getCodelistState(codelistId, AsyncCallBackWrapper.wrap(new ManagedFailureCallback<String>() {

			@Override
			public void onSuccess(String result) {
				updateState(result);
			}
		}));
	}
	
	@EventHandler
	void onNewState(NewStateEvent event) {
		updateState(event.getState());
	}
	
	private void showAllGroupsAsColumn() {
		view.getCodeListEditor().showAllGroupsAsColumn();
	}
	
	private void updateState(String state) {
		view.getToolBar().setState(state);
		view.getToolBar().showStateLoader(false);
	}

	
	private void bindFeatures()
	{
		
		// CODELIST EDITOR
		FeatureBinder.bind((HasEditing)view.getCodeListEditor(), codelistId, ManagerUIFeature.EDIT_CODELIST);
		
		//ATTRIBUTES EDITOR
		FeatureBinder.bind(view.getAttributesEditor(), codelistId, ManagerUIFeature.EDIT_CODELIST);
		
		//LINKS EDITOR
		FeatureBinder.bind(view.getLinksEditor(), codelistId, ManagerUIFeature.EDIT_CODELIST);
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}
	
	public Widget getView() {
		return view.asWidget();
	}
}
