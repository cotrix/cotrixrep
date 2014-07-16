package org.cotrix.web.manage.client.codelist;

import java.util.Collection;

import org.cotrix.web.common.client.Presenter;
import org.cotrix.web.common.client.async.AsyncUtils;
import org.cotrix.web.common.client.async.AsyncUtils.SuccessCallback;
import org.cotrix.web.common.client.widgets.LoaderPanel;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.common.shared.codelist.attributetype.UIAttributeType;
import org.cotrix.web.common.shared.codelist.linktype.AttributeValue;
import org.cotrix.web.common.shared.codelist.linktype.CodeNameValue;
import org.cotrix.web.common.shared.codelist.linktype.LinkValue;
import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.common.shared.codelist.linktype.UILinkType.UIValueType;
import org.cotrix.web.manage.client.codelist.cache.AttributeTypesCache;
import org.cotrix.web.manage.client.codelist.cache.LinkTypesCache;
import org.cotrix.web.manage.client.codelist.codes.CodesPanelPresenter;
import org.cotrix.web.manage.client.codelist.event.CodelistLinkRefreshedEvent;
import org.cotrix.web.manage.client.codelist.event.ReadyEvent;
import org.cotrix.web.manage.client.codelist.metadata.MetadataPanelPresenter;
import org.cotrix.web.manage.client.data.AttributeTypeBridge;
import org.cotrix.web.manage.client.data.CodeAttribute;
import org.cotrix.web.manage.client.data.CodeAttributeBridge;
import org.cotrix.web.manage.client.data.CodeBridge;
import org.cotrix.web.manage.client.data.CodeLink;
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
	
	@Inject @CodelistBus
	private EventBus codelistBus;

	@Inject
	private CodesPanelPresenter codesPresenter;
	private boolean codesDirty = false;
	private boolean codesHeaderDirty = false;

	@Inject
	private MetadataPanelPresenter metadataPresenter;

	private LoaderPanel loadingPanel = new LoaderPanel();

	@Inject @CurrentCodelist
	private LinkTypesCache linkTypesCache;

	@Inject @CurrentCodelist
	private AttributeTypesCache attributeTypesCache;
	
	private int cacheToLoad = 2;

	@Inject
	private void init() {
		view = new DeckLayoutPanel();
		view.setAnimationVertical(true);
		codesPresenter.go(view);
		metadataPresenter.go(view);
		
		loadingPanel = new LoaderPanel();
		loadingPanel.setMessage("loading "+codelist.getName().getLocalPart()+" ...");
		view.add(loadingPanel);

		loadCaches();
	}

	private void loadCaches() {
		showLoader();
		linkTypesCache.setup(AsyncUtils.manageError(new SuccessCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				Log.trace("link types loaded "+linkTypesCache);
				checkLoading();
			}
		}));
		attributeTypesCache.setup(AsyncUtils.manageError(new SuccessCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				Log.trace("attribute types loaded "+attributeTypesCache);
				checkLoading();
			}
		}));
	}
	
	private void checkLoading() {
		cacheToLoad--;
		if (cacheToLoad == 0) setupComplete();
	}
	
	private void setupComplete() {
		Log.trace("completing setup");
		codelistBus.fireEvent(new ReadyEvent());
		showCodes();
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

	public void onSelected() {
		checkCodesDirty();
	}

	private void showLoader() {
		view.showWidget(loadingPanel);
	}

	private void showCodes() {
		checkCodesDirty();
		view.showWidget(codesPresenter.getView());
	}

	private void checkCodesDirty() {
		Log.trace("codesDirty: "+codesDirty);
		if (codesDirty) {
			codesPresenter.reloadCodes(codesHeaderDirty);
			codesDirty = false;
			codesHeaderDirty = false;
		}
	}

	@Inject
	private void bind(final @ManagerBus EventBus eventBus) {
		eventBus.addHandler(DataSavedEvent.TYPE, new DataSavedEvent.DataSavedHandler() {

			@Override
			public void onDataSaved(final DataSavedEvent event) {
				Log.trace(codelist.getName().getLocalPart()+" onDataSaved event: "+event);
				
				//the modification is about our codelist
				if (isAboutOurCodelist(event)) {
					codesDirty |= isUpdateOrRemoveOf(event, UIAttributeType.class, UILinkType.class);
					codesHeaderDirty |= isUpdateOrRemoveOf(event, UIAttributeType.class);

				} else {
						
					if (!isUpdateOrRemoveOf(event, UICode.class, CodeAttribute.class, CodeLink.class, UILinkType.class)) return;
					
					//we check if the modified item is referred by our link types
					UILinkType referencedLink = getOurLinkTypesReferTheModifiedItem(event, linkTypesCache.getItems());
					Log.trace(codelist.getName().getLocalPart()+" referencedLink: "+referencedLink);
					
					codesDirty |= referencedLink!=null;
					
					//we inform other codelists about the update
					if (referencedLink!=null) eventBus.fireEvent(new CodelistLinkRefreshedEvent(codelist.getId(), referencedLink));

				}
			}
		});

		eventBus.addHandler(CodelistLinkRefreshedEvent.TYPE, new CodelistLinkRefreshedEvent.CodelistLinkRefreshedHandler() {

			@Override
			public void onLinkRefreshed(final CodelistLinkRefreshedEvent event) {
				Log.trace(codelist.getName().getLocalPart()+" onLinkRefreshed event: "+event);
				
				if (event.getCodelistId().equals(codelist.getId())) return;
						
				UILinkType ourLinkType = ourLinkTypesReferTheModifiedCodelistLinkType(linkTypesCache.getItems(), event.getCodelistId(), event.getLinkType());
				Log.trace(codelist.getName().getLocalPart()+" ourLinkType: "+ourLinkType);
				
				codesDirty |= ourLinkType!=null;
				if (ourLinkType!=null) eventBus.fireEvent(new CodelistLinkRefreshedEvent(codelist.getId(), ourLinkType));
			}
		});
	}

	private boolean isUpdateOrRemoveOf(DataSavedEvent event, Class<?> ... clazzes) {
		if (!isUpdateOrRemove(event)) return false;
		DataEditEvent<?> editEvent = event.getEditEvent();
		for (Class<?> clazz:clazzes) if (editEvent.getData().getClass() == clazz) return true;
		return false;
	}

	private boolean isUpdateOrRemove(DataSavedEvent event) {
		DataEditEvent<?> editEvent = event.getEditEvent();
		return editEvent.getEditType() == EditType.UPDATE || editEvent.getEditType() == EditType.REMOVE;
	}

	private boolean isAboutOurCodelist(DataSavedEvent event) {
		return codelist.getId().equals(event.getCodelistId());
	}

	private UILinkType getOurLinkTypesReferTheModifiedItem(DataSavedEvent event, Collection<UILinkType> linkTypes) {
		DataEditEvent<?> editEvent = event.getEditEvent();
		String targetCodelistId = event.getCodelistId();

		//Log.trace("editEvent: "+editEvent);
		for (UILinkType linkType:linkTypes) {
			//Log.trace("checking link type: "+linkType);
			if (targetCodelistId.equals(linkType.getTargetCodelist().getId())) {

				UIValueType valueType = linkType.getValueType();
				//Log.trace("valueType: "+valueType);

				//we reference the code, the attribute, the link (instance or type)
				if (isCodeRelated(valueType, editEvent)
						|| isAttributeRelated(valueType, editEvent)
						|| isLinkRelated(valueType, editEvent)
						|| isLinkTypeRelated(valueType, editEvent)) return linkType;
			}
		}

		return null;
	}

	private boolean isCodeRelated(UIValueType valueType, DataEditEvent<?> editEvent) {
		return valueType instanceof CodeNameValue && editEvent.getData() instanceof UICode;
	}

	private boolean isAttributeRelated(UIValueType valueType, DataEditEvent<?> editEvent) {
		return valueType instanceof AttributeValue 
				&& editEvent.getData() instanceof CodeAttribute 
				&&  match((AttributeValue)valueType, (CodeAttribute)editEvent.getData());
	}

	private boolean isLinkRelated(UIValueType valueType, DataEditEvent<?> editEvent) {
		return valueType instanceof LinkValue 
				&& editEvent.getData() instanceof CodeLink 
				&& match((LinkValue)valueType, (CodeLink)editEvent.getData());
	}

	private boolean isLinkTypeRelated(UIValueType valueType, DataEditEvent<?> editEvent) {
		return valueType instanceof LinkValue 
				&& editEvent.getData() instanceof UILinkType 
				&& ((LinkValue)valueType).getLinkId().equals(((UILinkType)editEvent.getData()).getId());
	}

	private boolean match(AttributeValue attributeValue, CodeAttribute codeAttribute) {

		UIAttribute attribute = codeAttribute.getAttribute();
		Log.trace("comparing attributeValue: "+attributeValue+" with attribute: "+attribute);

		if (attributeValue.getLanguage() == null) {
			if (attribute.getLanguage() != null)
				return false;
		} else if (!attributeValue.getLanguage().equals(attribute.getLanguage()))
			return false;

		if (attributeValue.getName() == null) {
			if (attribute.getName() != null)
				return false;
		} else if (!attributeValue.getName().equals(attribute.getName()))
			return false;
		if (attributeValue.getType() == null) {
			if (attribute.getType() != null)
				return false;
		} else if (!attributeValue.getType().equals(attribute.getType()))
			return false;
		return true;
	}

	private boolean match(LinkValue linkvalue, CodeLink codeLink) {
		UILink link = codeLink.getLink();
		return linkvalue.getLinkId().equals(link.getTypeId());
	}
	
	private UILinkType ourLinkTypesReferTheModifiedCodelistLinkType(Collection<UILinkType> linkTypes, String codelistId, UILinkType type) {
		for (UILinkType linkType:linkTypes) {
			//our link type refers the modified codelist
			if (codelistId.equals(linkType.getTargetCodelist().getId())
					//is a link of link
					&& linkType.getValueType() instanceof LinkValue
					//the link target the modified link type
					&& ((LinkValue)linkType.getValueType()).getLinkId().equals(type.getId())) {
				 return linkType;
			}
		}
		
		return null;
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public Widget getView() {
		return view;
	}
}
