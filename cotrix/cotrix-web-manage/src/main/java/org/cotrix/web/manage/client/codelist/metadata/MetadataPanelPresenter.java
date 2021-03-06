package org.cotrix.web.manage.client.codelist.metadata;

import org.cotrix.web.common.client.Presenter;
import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.feature.AsyncCallBackWrapper;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.HasFeature;
import org.cotrix.web.common.shared.codelist.LifecycleState;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.feature.AbstractFeatureCarrier;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.codelist.CodelistNewStateEvent;
import org.cotrix.web.manage.client.codelist.NewStateEvent;
import org.cotrix.web.manage.client.codelist.SwitchPanelEvent;
import org.cotrix.web.manage.client.codelist.metadata.MetadataToolbar.ToolBarListener;
import org.cotrix.web.manage.client.codelist.metadata.SplashPanel.Action;
import org.cotrix.web.manage.client.codelist.metadata.SplashPanel.Listener;
import org.cotrix.web.manage.client.data.DataSaverManager;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.event.ManagerBus;
import org.cotrix.web.manage.shared.ManagerUIFeature;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataPanelPresenter implements Presenter {

	private MetadataPanelView view;
	private String codelistId;
	private ManageServiceAsync service;
	
	@Inject
	private DataSaverManager saverManager;
	
	@Inject @CurrentCodelist
	private UICodelist codelist;

	@Inject @CodelistBus 
	private EventBus codelistBus;
	
	@Inject @ManagerBus
	private EventBus managerBus;
	
	private FeatureBinder featureBinder;
	
	private ManagedFailureCallback<AbstractFeatureCarrier.Void> callBack = new ManagedFailureCallback<AbstractFeatureCarrier.Void>() {

		@Override
		public void onSuccess(AbstractFeatureCarrier.Void result) {
			loadState();
		}
	};

	@Inject
	public MetadataPanelPresenter(MetadataPanelView view, @CurrentCodelist String codelistId, ManageServiceAsync service, DataSaverManager saverManager, FeatureBinder featureBinder) {
		this.view = view;
		this.codelistId = codelistId;
		this.service = service;
		this.saverManager = saverManager;
		this.featureBinder = featureBinder;
		
		bind();
		bindFeatures();
		loadState();
	}
	
	private void bind()
	{
		// TOOLBAR
		MetadataToolbar toolbar = view.getToolBar();
		
		toolbar.setListener(new ToolBarListener() {
			
			@Override
			public void onAction(org.cotrix.web.manage.client.codelist.metadata.MetadataToolbar.Action action) {
				Log.trace("toolbar onAction "+action);
				switch (action) {
					case TO_CODES: codelistBus.fireEvent(SwitchPanelEvent.CODES); break;
				}
			}
		});
		
		SplashPanel splashPanel = view.getSplashPanel();
		splashPanel.setListener(new Listener() {
			
			@Override
			public void onAction(Action action) {
				Log.trace("splash onAction "+action);
				switch (action) {
					case LOCK: lock(); break;
					case UNLOCK: unlock(); break;
					case SEAL: sealCodelist(); break;
					case UNSEAL: unsealCodelist(); break;
				}
			}
		});
	}
	
	private void lock()
	{
		service.lock(codelistId, callBack);
	}
	
	private void unlock()
	{
		service.unlock(codelistId, callBack);
	}
	
	private void sealCodelist()
	{
		service.seal(codelistId, callBack);
	}
	
	private void unsealCodelist()
	{
		service.unseal(codelistId, callBack);
	}
	
	private void loadState() {
		service.getCodelistState(codelistId, AsyncCallBackWrapper.wrap(new ManagedFailureCallback<LifecycleState>() {

			@Override
			public void onSuccess(LifecycleState result) {
				codelistBus.fireEvent(new NewStateEvent(result));
				managerBus.fireEvent(new CodelistNewStateEvent(codelist, result));
			}
		}));
	}

	
	private void bindFeatures()
	{

		SplashPanel splashPanel = view.getSplashPanel();
		
		featureBinder.bind(new ActionEnabler(Action.LOCK, splashPanel), codelistId, ManagerUIFeature.LOCK_CODELIST);
		featureBinder.bind(new ActionEnabler(Action.UNLOCK, splashPanel), codelistId, ManagerUIFeature.UNLOCK_CODELIST);
		featureBinder.bind(new ActionEnabler(Action.SEAL, splashPanel), codelistId, ManagerUIFeature.SEAL_CODELIST);
		featureBinder.bind(new ActionEnabler(Action.UNSEAL, splashPanel), codelistId, ManagerUIFeature.UNSEAL_CODELIST);
		
		//ATTRIBUTES EDITOR
		featureBinder.bind(view.getAttributesEditor(), codelistId, ManagerUIFeature.EDIT_METADATA);
		
		//LINK TYPES EDITOR
		featureBinder.bind(view.getLinkTypesEditor(), codelistId, ManagerUIFeature.EDIT_CODELIST);
		
		//ATTRIBUTE TYPES EDITOR
		featureBinder.bind(view.getAttributeTypesPanel(), codelistId, ManagerUIFeature.EDIT_CODELIST);
	}
	
	private class ActionEnabler implements HasFeature {
		protected Action action;
		protected SplashPanel splashPanel;

		public ActionEnabler(Action action, SplashPanel splashPanel) {
			this.action = action;
			this.splashPanel = splashPanel;
		}

		@Override
		public void setFeature() {
			splashPanel.setEnabled(action, true);
		}

		@Override
		public void unsetFeature() {
			splashPanel.setEnabled(action, false);
		}
		
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public Widget getView() {
		return view.asWidget();
	}
}
