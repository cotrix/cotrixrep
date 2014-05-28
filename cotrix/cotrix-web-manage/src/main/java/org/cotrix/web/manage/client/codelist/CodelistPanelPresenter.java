package org.cotrix.web.manage.client.codelist;

import org.cotrix.web.common.client.Presenter;
import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.feature.AsyncCallBackWrapper;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.HasFeature;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.feature.FeatureCarrier;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.codelist.CodesToolbar.Action;
import org.cotrix.web.manage.client.codelist.CodesToolbar.ToolBarListener;
import org.cotrix.web.manage.client.data.AttributeTypeModifyGenerator;
import org.cotrix.web.manage.client.data.CodeAttributeCommandGenerator;
import org.cotrix.web.manage.client.data.CodeLinkCommandGenerator;
import org.cotrix.web.manage.client.data.CodeModifyCommandGenerator;
import org.cotrix.web.manage.client.data.DataSaverManager;
import org.cotrix.web.manage.client.data.LinkTypeModifyGenerator;
import org.cotrix.web.manage.client.data.MetadataAttributeModifyGenerator;
import org.cotrix.web.manage.client.data.MetadataModifyCommandGenerator;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.event.ManagerBus;
import org.cotrix.web.manage.shared.ManagerUIFeature;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistPanelPresenter implements Presenter {

	private CodelistPanelView view;
	private String codelistId;
	private ManageServiceAsync service;
	
	@Inject
	private DataSaverManager saverManager;
	
	@Inject @CurrentCodelist
	private UICodelist codelist;
	
	@Inject @ManagerBus
	private EventBus managerBus;
	
	private ManagedFailureCallback<FeatureCarrier.Void> callBack = new ManagedFailureCallback<FeatureCarrier.Void>() {

		@Override
		public void onSuccess(FeatureCarrier.Void result) {
			loadState();
		}
	};

	@Inject
	public CodelistPanelPresenter(CodelistPanelView view, @CurrentCodelist String codelistId, ManageServiceAsync service, DataSaverManager saverManager) {
		this.view = view;
		this.codelistId = codelistId;
		this.service = service;
		this.saverManager = saverManager;
		
		bind();
		bindFeatures();
		bindSavers();
		
		showAllGroupsAsColumn();
		loadState();
	}
	
	

	private void bindSavers() {
		saverManager.register(new CodeModifyCommandGenerator());
		saverManager.register(new CodeAttributeCommandGenerator());
		saverManager.register(new MetadataModifyCommandGenerator());
		saverManager.register(new MetadataAttributeModifyGenerator());
		saverManager.register(new LinkTypeModifyGenerator());
		saverManager.register(new CodeLinkCommandGenerator());
		saverManager.register(new AttributeTypeModifyGenerator());
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
					case LOCK: lock(); break;
					case FINALIZE: finalizeCodelist(); break;
					case UNLOCK: unlock(); break;
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
	
	private void showAllGroupsAsColumn() {
		view.getCodeListEditor().showAllGroupsAsColumn();
	}
	
	private void lock()
	{
		view.getToolBar().showStateLoader(true);
		service.lock(codelistId, callBack);
	}
	
	private void unlock()
	{
		view.getToolBar().showStateLoader(true);
		service.unlock(codelistId, callBack);
	}
	
	private void finalizeCodelist()
	{
		view.getToolBar().showStateLoader(true);
		service.seal(codelistId, callBack);
	}
	
	private void updateState(String state) {
		view.getToolBar().setState(state);
		view.getToolBar().showStateLoader(false);
	}

	
	private void bindFeatures()
	{
		// TOOLBAR
		CodesToolbar toolbar = view.getToolBar();
		
		FeatureBinder.bind(new ActionEnabler(Action.LOCK, toolbar), codelistId, ManagerUIFeature.LOCK_CODELIST);
		FeatureBinder.bind(new ActionEnabler(Action.UNLOCK, toolbar), codelistId, ManagerUIFeature.UNLOCK_CODELIST);
		FeatureBinder.bind(new ActionEnabler(Action.FINALIZE, toolbar), codelistId, ManagerUIFeature.SEAL_CODELIST);
		
		// CODELIST EDITOR
		FeatureBinder.bind((HasEditing)view.getCodeListEditor(), codelistId, ManagerUIFeature.EDIT_CODELIST);
		
		//METADATA EDITOR
		FeatureBinder.bind(view.getMetadataEditor(), codelistId, ManagerUIFeature.EDIT_METADATA);
		
		//ATTRIBUTES EDITOR
		FeatureBinder.bind(view.getAttributesEditor(), codelistId, ManagerUIFeature.EDIT_CODELIST);
		
		//LINK TYPES EDITOR
		FeatureBinder.bind(view.getLinkTypesEditor(), codelistId, ManagerUIFeature.EDIT_CODELIST);
		
		//LINKS EDITOR
		FeatureBinder.bind(view.getLinksEditor(), codelistId, ManagerUIFeature.EDIT_CODELIST);
		
		//ATTRIBUTE TYPES EDITOR
		FeatureBinder.bind(view.getAttributeTypesPanel(), codelistId, ManagerUIFeature.EDIT_CODELIST);
	}
	
	private class ActionEnabler implements HasFeature {
		protected Action action;
		protected CodesToolbar toolbar;

		/**
		 * @param action
		 * @param toolbar
		 */
		public ActionEnabler(Action action, CodesToolbar toolbar) {
			this.action = action;
			this.toolbar = toolbar;
		}

		@Override
		public void setFeature() {
			toolbar.setEnabled(action, true);
		}

		@Override
		public void unsetFeature() {
			toolbar.setEnabled(action, false);
		}
		
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public CodelistPanelView getView() {
		return view;
	}
}
