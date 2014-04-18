/**
 * 
 */
package org.cotrix.web.manage.client.codelist.link;

import java.util.List;

import org.cotrix.web.common.client.widgets.EditableLabel;
import org.cotrix.web.common.client.widgets.table.CellContainer;
import org.cotrix.web.common.client.widgets.table.Table;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.linktype.CodeNameType;
import org.cotrix.web.manage.client.codelist.attribute.AttributesPanel;
import org.cotrix.web.manage.client.codelist.link.CodeSuggestOracle.CodeSuggestion;
import org.cotrix.web.manage.client.codelist.link.LinkTypeSuggestOracle.LinkTypeSuggestion;
import org.cotrix.web.manage.shared.UICodeInfo;
import org.cotrix.web.manage.shared.UILinkTypeInfo;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkDetailsPanel extends Composite implements HasValueChangeHandlers<Void>{

	public static final String CODE_NAME_VALUE_TYPE = Document.get().createUniqueId();
	public static final CodeNameType CODE_NAME_TYPE = new CodeNameType();

	private static LinkDetailsPanelUiBinder uiBinder = GWT.create(LinkDetailsPanelUiBinder.class);

	interface LinkDetailsPanelUiBinder extends UiBinder<Widget, LinkDetailsPanel> {}

	interface Style extends CssResource {
		String error();
		String editor();
	}

	@UiField Table table;

	@UiField EditableLabel typeBoxContainer;
	@UiField(provided=true) SuggestBox typeBox;
	@UiField Image typeBoxLoader;
	private LinkTypeSuggestOracle linkTypeSuggestOracle;
	private UILinkTypeInfo selectedLinkType;

	@UiField EditableLabel targetCodeBoxContainer;
	@UiField(provided=true) SuggestBox targetCodeBox;
	@UiField Image targetCodeBoxLoader;
	private CodeSuggestOracle codeSuggestOracle;
	private UICodeInfo selectedCode;

	@UiField CellContainer valueRow;
	@UiField Label valueLabel;
	@UiField Image valueLabelLoader;

	private AttributesPanel attributesPanel;

	@UiField Style style;

	private LinksCodelistInfoProvider codelistInfoProvider;

	public LinkDetailsPanel(LinksCodelistInfoProvider codelistInfoProvider) {

		this.codelistInfoProvider = codelistInfoProvider;
		linkTypeSuggestOracle = new LinkTypeSuggestOracle();
		codeSuggestOracle = new CodeSuggestOracle();

		createLinkTypesBox();
		createTargetCodeBox();

		initWidget(uiBinder.createAndBindUi(this));

		setupAttributesPanel();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				loadLinkTypes();
			}
		});
	}	

	private void createLinkTypesBox() {
		typeBox = new SuggestBox(linkTypeSuggestOracle);
		typeBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				selectedLinkType = null;
				fireChange();
			}
		});

		typeBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				LinkTypeSuggestion suggestion = (LinkTypeSuggestion) event.getSelectedItem();
				selectedLinkType = suggestion.getLinkType();
				loadCodes(selectedLinkType.getId());
				typeBoxContainer.setText(LinkTypeSuggestion.toDisplayString(selectedLinkType));
				fireChange();
			}
		});
	}

	public void setLinkType(String typeId, UIQName name) {
		if (typeId!=null) {
			selectedLinkType = new UILinkTypeInfo(typeId, name);
			typeBox.getValueBox().setValue(LinkTypeSuggestion.toDisplayString(selectedLinkType), false);
			typeBoxContainer.setText(LinkTypeSuggestion.toDisplayString(selectedLinkType));
			loadCodes(typeId);
		}
	}

	public UILinkTypeInfo getLinkType() {
		return selectedLinkType;
	}

	public void setValidLinkType(boolean valid) {
		typeBox.setStyleName(style.error(), !valid);
	}

	public void setLinkTypeReadonly(boolean readOnly) {
		typeBoxContainer.setReadOnly(readOnly);
		if (readOnly) typeBox.setStyleName(style.error(), false);
	}

	private final AsyncCallback<List<UILinkTypeInfo>> linkTypesCallBack = new AsyncCallback<List<UILinkTypeInfo>>() {

		@Override
		public void onSuccess(List<UILinkTypeInfo> result) {
			linkTypeSuggestOracle.loadCache(result);
			typeBoxLoader.setVisible(false);
			typeBoxContainer.setVisible(true);
		}

		@Override
		public void onFailure(Throwable caught) {

		}
	};

	private void loadLinkTypes() {
		typeBoxLoader.setVisible(true);
		typeBoxContainer.setVisible(false);
		codelistInfoProvider.getCodelistLinkTypes(linkTypesCallBack);
	}


	private void createTargetCodeBox() {
		targetCodeBox = new SuggestBox(codeSuggestOracle);
		targetCodeBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				selectedCode = null;
				fireChange();
			}
		});

		targetCodeBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				CodeSuggestion suggestion = (CodeSuggestion) event.getSelectedItem();
				selectedCode = suggestion.getCode();
				targetCodeBoxContainer.setText(CodeSuggestion.toDisplayString(selectedCode));
				fireChange();
			}
		});
	}

	public void setCode(String codeId, UIQName name) {
		if (codeId!=null) {
			selectedCode = new UICodeInfo(codeId, name);
			targetCodeBox.getValueBox().setValue(CodeSuggestion.toDisplayString(selectedCode), false);
			targetCodeBoxContainer.setText(CodeSuggestion.toDisplayString(selectedCode));
		}
	}

	public UICodeInfo getCode() {
		return selectedCode;
	}

	public void setValidCode(boolean valid) {
		targetCodeBox.setStyleName(style.error(), !valid);
	}

	public void setCodeReadonly(boolean readOnly) {
		targetCodeBoxContainer.setReadOnly(readOnly);
		if (readOnly) targetCodeBox.setStyleName(style.error(), false);
	}

	private final AsyncCallback<List<UICodeInfo>> codesCallBack = new AsyncCallback<List<UICodeInfo>>() {

		@Override
		public void onSuccess(List<UICodeInfo> result) {
			Log.trace("loaded "+result.size()+" codes");
			codeSuggestOracle.loadCache(result);
			targetCodeBoxLoader.setVisible(false);
			targetCodeBoxContainer.setVisible(true);
		}

		@Override
		public void onFailure(Throwable caught) {

		}
	};

	private void loadCodes(String typeId) {
		targetCodeBoxLoader.setVisible(true);
		targetCodeBoxContainer.setVisible(false);
		codelistInfoProvider.getCodelistCodes(typeId, codesCallBack);
	}	


	public void setValue(String value) {
		valueLabel.setText(value);
	}

	public void setValueLoaderVisible(boolean visible) {
		valueLabelLoader.setVisible(visible);
		valueLabel.setVisible(!visible);
	}

	public void setValueVisible(boolean visible) {
		valueRow.setVisible(visible);
	}


	private void setupAttributesPanel() {
		attributesPanel = new AttributesPanel(table, style.error());
		attributesPanel.addValueChangeHandler(new ValueChangeHandler<Void>() {

			@Override
			public void onValueChange(ValueChangeEvent<Void> event) {
				fireChange();
			}
		});
	}


	public void setAttributes(List<UIAttribute> attributes) {
		attributesPanel.setAttributes(attributes);
	}

	public List<UIAttribute> getAttributes() {
		return attributesPanel.getAttributes();
	}

	public boolean areAttributesValid() {
		return attributesPanel.areValid();
	}


	public void setReadOnly(boolean readOnly) {

		typeBoxContainer.setReadOnly(readOnly);
		if (readOnly) typeBox.setStyleName(style.error(), false);

		targetCodeBoxContainer.setReadOnly(readOnly);
		if (readOnly) targetCodeBox.setStyleName(style.error(), false);

		attributesPanel.setReadOnly(readOnly);
	}

	private void fireChange() {
		ValueChangeEvent.fire(this, null);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Void> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

}
