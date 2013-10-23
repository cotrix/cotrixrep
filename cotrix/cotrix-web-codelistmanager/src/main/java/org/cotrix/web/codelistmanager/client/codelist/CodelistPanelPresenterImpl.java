package org.cotrix.web.codelistmanager.client.codelist;

import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.client.codelist.CodelistToolbar.Action;
import org.cotrix.web.codelistmanager.client.codelist.CodelistToolbar.ToolBarListener;
import org.cotrix.web.codelistmanager.client.data.CodeAttributeCommandGenerator;
import org.cotrix.web.codelistmanager.client.data.CodeModifyCommandGenerator;
import org.cotrix.web.codelistmanager.client.data.DataSaverManager;
import org.cotrix.web.codelistmanager.client.data.MetadataAttributeModifyGenerator;
import org.cotrix.web.codelistmanager.client.data.MetadataModifyCommandGenerator;
import org.cotrix.web.codelistmanager.shared.ManagerUIFeature;
import org.cotrix.web.share.client.feature.FeatureBinder;
import org.cotrix.web.share.client.feature.HasFeature;
import org.cotrix.web.share.client.rpc.Nop;
import org.cotrix.web.share.shared.feature.FeatureCarrier;
import org.cotrix.web.share.shared.feature.Request;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistPanelPresenterImpl implements CodelistPanelPresenter {

	protected CodelistPanelView view;
	protected String codelistId;
	@Inject
	protected ManagerServiceAsync service;
	
	@Inject
	protected DataSaverManager saverManager;

	@Inject
	public CodelistPanelPresenterImpl(CodelistPanelView view, @CodelistId String codelistId, DataSaverManager saverManager) {
		this.view = view;
		this.codelistId = codelistId;
		this.saverManager = saverManager;
		bindFeatures();
		bindSavers();
	}

	protected void bindSavers() {
		saverManager.register(new CodeModifyCommandGenerator());
		saverManager.register(new CodeAttributeCommandGenerator());
		saverManager.register(new MetadataModifyCommandGenerator());
		saverManager.register(new MetadataAttributeModifyGenerator());
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	@Override
	public CodelistPanelView getView() {
		return view;
	}
	
	protected void bindFeatures()
	{
		CodelistToolbar toolbar = view.getToolBar();
		
		toolbar.setListener(new ToolBarListener() {
			
			@Override
			public void onAction(Action action) {
				Log.trace("toolbar onAction "+action);
				switch (action) {
					case ALL_COLUMN: view.getCodeListEditor().showAllAttributesAsColumn(); break;
					case ALL_NORMAL: view.getCodeListEditor().showAllAttributesAsNormal(); break;
					case LOCK: service.lock(Request.voidRequest(codelistId), Nop.<FeatureCarrier.Void>getInstance()); break;
					case FINALIZE: service.seal(Request.voidRequest(codelistId), Nop.<FeatureCarrier.Void>getInstance()); break;
					case UNLOCK: service.unlock(Request.voidRequest(codelistId), Nop.<FeatureCarrier.Void>getInstance()); break;
				}
			}
		});
		
		FeatureBinder.bind(new ActionEnabler(Action.LOCK, toolbar), codelistId, ManagerUIFeature.LOCK_CODELIST);
		FeatureBinder.bind(new ActionEnabler(Action.UNLOCK, toolbar), codelistId, ManagerUIFeature.UNLOCK_CODELIST);
		FeatureBinder.bind(new ActionEnabler(Action.FINALIZE, toolbar), codelistId, ManagerUIFeature.SEAL_CODELIST);
	}
	
	protected class ActionEnabler implements HasFeature {
		protected Action action;
		protected CodelistToolbar toolbar;

		/**
		 * @param action
		 * @param toolbar
		 */
		public ActionEnabler(Action action, CodelistToolbar toolbar) {
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


}
