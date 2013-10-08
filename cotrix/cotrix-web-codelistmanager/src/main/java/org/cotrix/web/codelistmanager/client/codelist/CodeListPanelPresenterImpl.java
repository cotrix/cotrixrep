package org.cotrix.web.codelistmanager.client.codelist;

import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.client.data.MetadataSaver;
import org.cotrix.web.codelistmanager.shared.ManagerUIFeature;
import org.cotrix.web.share.client.event.FeatureAsyncCallBack;
import org.cotrix.web.share.client.feature.FeatureBinder;
import org.cotrix.web.share.shared.feature.Request;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListPanelPresenterImpl implements CodeListPanelPresenter {

	protected CodeListPanelView view;
	protected String codelistId;
	@Inject
	protected ManagerServiceAsync service;
	
	@Inject
	protected MetadataSaver metadataSaver;

	@Inject
	public CodeListPanelPresenterImpl(CodeListPanelView view, @CodelistId String codelistId) {
		this.view = view;
		this.codelistId = codelistId;
		bindFeatures();
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	@Override
	public CodeListPanelView getView() {
		return view;
	}
	
	protected void bindFeatures()
	{
		CodeListToolbar toolbar = view.getToolBar();
		FeatureBinder.bind((HasEnabled)toolbar.getLockButton(), codelistId, ManagerUIFeature.LOCK_CODELIST);
		toolbar.getLockButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				service.lock(Request.voidRequest(codelistId), FeatureAsyncCallBack.<Void>nop());
			}
		});

		FeatureBinder.bind((HasEnabled)toolbar.getUnlockButton(), codelistId, ManagerUIFeature.UNLOCK_CODELIST);
		toolbar.getUnlockButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				service.unlock(Request.voidRequest(codelistId), FeatureAsyncCallBack.<Void>nop());
			}
		});

		FeatureBinder.bind((HasEnabled)toolbar.getSealButton(), codelistId, ManagerUIFeature.SEAL_CODELIST);
		toolbar.getSealButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				service.seal(Request.voidRequest(codelistId), FeatureAsyncCallBack.<Void>nop());
			}
		});
		
		toolbar.getAllColumns().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				view.getCodeListEditor().showAllAttributesAsColumn();			
			}
		});
		
		toolbar.getAllNormals().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				view.getCodeListEditor().showAllAttributesAsNormal();
			}
		});
	}


}
