package org.cotrix.web.codelistmanager.client.presenter;

import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.client.view.CodeListPanelView;
import org.cotrix.web.codelistmanager.client.view.CodeListRowDataProvider;
import org.cotrix.web.codelistmanager.client.view.CodeListToolbar;
import org.cotrix.web.codelistmanager.shared.ManagerUIFeature;
import org.cotrix.web.share.client.event.FeatureAsyncCallBack;
import org.cotrix.web.share.client.feature.FeatureBinder;
import org.cotrix.web.share.shared.UICodelist;
import org.cotrix.web.share.shared.feature.Request;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListPanelPresenterImpl implements CodeListPanelPresenter {

	protected CodeListPanelView view;
	protected UICodelist codelist;
	protected ManagerServiceAsync service;


	@Inject
	public CodeListPanelPresenterImpl(CodeListPanelView view, @Assisted UICodelist codelist, ManagerServiceAsync service) {
		this.view = view;
		this.codelist = codelist;
		this.service = service;

		setProvider();
		bindFeatures();
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	@Override
	public CodeListPanelView getView() {
		return view;
	}

	public void setProvider()
	{
		CodeListRowDataProvider provider = new CodeListRowDataProvider(codelist.getId());
		view.setProvider(provider);
	}

	protected void bindFeatures()
	{
		CodeListToolbar toolbar = view.getToolBar();
		FeatureBinder.bind((HasEnabled)toolbar.getLockButton(), codelist.getId(), ManagerUIFeature.LOCK_CODELIST);
		toolbar.getLockButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				service.lock(Request.voidRequest(codelist.getId()), FeatureAsyncCallBack.<Void>nop());
			}
		});

		FeatureBinder.bind((HasEnabled)toolbar.getUnlockButton(), codelist.getId(), ManagerUIFeature.UNLOCK_CODELIST);
		toolbar.getUnlockButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				service.unlock(Request.voidRequest(codelist.getId()), FeatureAsyncCallBack.<Void>nop());
			}
		});

		FeatureBinder.bind((HasEnabled)toolbar.getSealButton(), codelist.getId(), ManagerUIFeature.SEAL_CODELIST);
		toolbar.getSealButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				service.seal(Request.voidRequest(codelist.getId()), FeatureAsyncCallBack.<Void>nop());
			}
		});
	}

}
