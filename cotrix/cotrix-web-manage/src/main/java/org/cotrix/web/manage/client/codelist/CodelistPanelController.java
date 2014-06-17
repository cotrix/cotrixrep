package org.cotrix.web.manage.client.codelist;

import org.cotrix.web.common.client.Presenter;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.attributetype.UIAttributeType;
import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
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
import org.cotrix.web.manage.client.data.event.DataEditEvent;
import org.cotrix.web.manage.client.data.event.DataSavedEvent;
import org.cotrix.web.manage.client.data.event.EditType;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.event.ManagerBus;

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
	private boolean codesDirty;
	
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
			case CODES: showCodes(); break;
			case METADATA: view.showWidget(metadataPresenter.getView()); break;
		}
	}
	
	private void showCodes() {
		Log.trace("codesDirty: "+codesDirty);
		if (codesDirty) {
			codesPresenter.reloadCodes();
			codesDirty = false;
		}
		view.showWidget(codesPresenter.getView());
	}
	
	@Inject
	private void bind(@ManagerBus EventBus eventBus) {
		eventBus.addHandler(DataSavedEvent.TYPE, new DataSavedEvent.DataSavedHandler() {
			
			@Override
			public void onDataSaved(DataSavedEvent event) {
				if (codelist.getId().equals(event.getCodelistId())) {
					DataEditEvent<?> editEvent = event.getEditEvent();
					codesDirty = isUpdateOrRemoveOf(editEvent, UIAttributeType.class, UILinkType.class);
				}
			}
		});
	}
	
	private boolean isUpdateOrRemoveOf(DataEditEvent<?> editEvent, Class<?> ... clazzes) {
		if (!(editEvent.getEditType() == EditType.UPDATE || editEvent.getEditType() == EditType.REMOVE)) return false;
		for (Class<?> clazz:clazzes) if (editEvent.getData().getClass() == clazz) return true;
		return false;
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public Widget getView() {
		return view;
	}
}
