package org.cotrix.web.manage.client.codelist;

import org.cotrix.web.common.client.Presenter;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.manage.client.codelist.codes.CodesPanelPresenter;
import org.cotrix.web.manage.client.codelist.metadata.MetadataPanelPresenter;
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

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
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
public class CodelistPanelController implements Presenter {
	
	interface CodelistPanelControllerEventBinder extends EventBinder<CodelistPanelController>{}

	private DeckLayoutPanel view;
	
	@Inject @CurrentCodelist
	private UICodelist codelist;
	
	@Inject
	private CodesPanelPresenter codesPresenter;
	
	@Inject
	private MetadataPanelPresenter metadataPresenter;
	
	@Inject
	private void init() {
		view = new DeckLayoutPanel();
		view.setAnimationVertical(true);
		codesPresenter.go(view);
		metadataPresenter.go(view);
		
		view.showWidget(0);
	}
	
	@Inject
	private void bindSavers(
			DataSaverManager saverManager,
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
	private void bind(CodelistPanelControllerEventBinder binder, @CodelistBus EventBus codelistBus) {
		binder.bindEventHandlers(this, codelistBus);
	}
	
	@EventHandler
	void onSwitchPanel(SwitchPanelEvent event) {
		Log.trace("onSwitchPanel "+event);
		switch (event.getTargetPanel()) {
			case CODES: view.showWidget(codesPresenter.getView()); break;
			case METADATA: view.showWidget(metadataPresenter.getView()); break;
		}
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public Widget getView() {
		return view;
	}
}
