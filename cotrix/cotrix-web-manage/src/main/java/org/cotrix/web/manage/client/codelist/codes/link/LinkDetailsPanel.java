/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.link;

import java.util.List;

import org.cotrix.web.common.client.widgets.table.CellContainer;
import org.cotrix.web.common.client.widgets.table.Table;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.linkdefinition.CodeNameValue;
import org.cotrix.web.manage.client.codelist.codes.link.CodeSuggestOracle.CodeSuggestion;
import org.cotrix.web.manage.client.codelist.codes.link.LinkDefinitionSuggestOracle.LinkDefinitionSuggestion;
import org.cotrix.web.manage.client.codelist.common.DetailsPanelStyle;
import org.cotrix.web.manage.client.codelist.common.SuggestListBox;
import org.cotrix.web.manage.client.codelist.common.attributed.AttributesPanel;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.client.util.Attributes;
import org.cotrix.web.manage.shared.UICodeInfo;
import org.cotrix.web.manage.shared.UILinkDefinitionInfo;

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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkDetailsPanel extends Composite implements HasValueChangeHandlers<Void>{

	public static final String CODE_NAME_VALUE_TYPE = Document.get().createUniqueId();
	public static final CodeNameValue CODE_NAME_TYPE = new CodeNameValue();

	private static LinkDetailsPanelUiBinder uiBinder = GWT.create(LinkDetailsPanelUiBinder.class);

	interface LinkDetailsPanelUiBinder extends UiBinder<Widget, LinkDetailsPanel> {}

	@UiField Table table;

	@UiField(provided=true) SuggestListBox definitionBox;
	@UiField Image definitionBoxLoader;
	private LinkDefinitionSuggestOracle linkDefinitionSuggestOracle;
	private UILinkDefinitionInfo selectedLinkDefinition;

	@UiField(provided=true) SuggestListBox targetCodeBox;
	@UiField Image targetCodeBoxLoader;
	private CodeSuggestOracle codeSuggestOracle;
	private UICodeInfo selectedCode;

	@UiField CellContainer valueRow;
	@UiField Label valueLabel;
	@UiField Image valueLabelLoader;

	private AttributesPanel attributesPanel;

	private DetailsPanelStyle style = CotrixManagerResources.INSTANCE.detailsPanelStyle();

	private LinksCodelistInfoProvider codelistInfoProvider;

	public LinkDetailsPanel(LinksCodelistInfoProvider codelistInfoProvider) {

		this.codelistInfoProvider = codelistInfoProvider;
		linkDefinitionSuggestOracle = new LinkDefinitionSuggestOracle();
		codeSuggestOracle = new CodeSuggestOracle();

		createLinkDefinitionBox();
		createTargetCodeBox();

		initWidget(uiBinder.createAndBindUi(this));

		setupAttributesPanel();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				loadLinkDefinitions();
			}
		});
	}	

	private void createLinkDefinitionBox() {
		definitionBox = new SuggestListBox(linkDefinitionSuggestOracle);
		definitionBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				selectedLinkDefinition = null;
				fireChange();
			}
		});

		definitionBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				LinkDefinitionSuggestion suggestion = (LinkDefinitionSuggestion) event.getSelectedItem();
				selectedLinkDefinition = suggestion.getLinkDefinition();
				loadCodes(selectedLinkDefinition.getId());
				fireChange();
			}
		});
	}

	public void setLinkDefinition(String definitionId, UIQName name) {
		if (definitionId!=null) {
			selectedLinkDefinition = new UILinkDefinitionInfo(definitionId, name);
			definitionBox.getValueBox().setValue(LinkDefinitionSuggestion.toDisplayString(selectedLinkDefinition), false);
			loadCodes(definitionId);
		}
	}

	public UILinkDefinitionInfo getLinkDefinition() {
		return selectedLinkDefinition;
	}

	public void setValidLinkDefinition(boolean valid) {
		definitionBox.setStyleName(style.textboxError(), !valid);
	}

	public void setLinkDefinitionReadonly(boolean readOnly) {
		definitionBox.setEnabled(!readOnly);
		if (readOnly) definitionBox.setStyleName(style.textboxError(), false);
	}

	private final AsyncCallback<List<UILinkDefinitionInfo>> linkDefinitionCallBack = new AsyncCallback<List<UILinkDefinitionInfo>>() {

		@Override
		public void onSuccess(List<UILinkDefinitionInfo> result) {
			linkDefinitionSuggestOracle.loadCache(result);
			definitionBoxLoader.setVisible(false);
			definitionBox.setVisible(true);
		}

		@Override
		public void onFailure(Throwable caught) {

		}
	};

	private void loadLinkDefinitions() {
		definitionBoxLoader.setVisible(true);
		definitionBox.setVisible(false);
		codelistInfoProvider.getCodelistLinkDefinitions(linkDefinitionCallBack);
	}


	private void createTargetCodeBox() {
		targetCodeBox = new SuggestListBox(codeSuggestOracle);
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
				fireChange();
			}
		});
	}

	public void setCode(String codeId, UIQName name) {
		if (codeId!=null) {
			selectedCode = new UICodeInfo(codeId, name);
			targetCodeBox.getValueBox().setValue(CodeSuggestion.toDisplayString(selectedCode), false);
		}
	}

	public UICodeInfo getCode() {
		return selectedCode;
	}

	public void setValidCode(boolean valid) {
		targetCodeBox.setStyleName(style.textboxError(), !valid);
	}

	public void setCodeReadonly(boolean readOnly) {
		targetCodeBox.setEnabled(!readOnly);
		if (readOnly) targetCodeBox.setStyleName(style.textboxError(), false);
	}

	private final AsyncCallback<List<UICodeInfo>> codesCallBack = new AsyncCallback<List<UICodeInfo>>() {

		@Override
		public void onSuccess(List<UICodeInfo> result) {
			Log.trace("loaded "+result.size()+" codes");
			codeSuggestOracle.loadCache(result);
			targetCodeBoxLoader.setVisible(false);
			targetCodeBox.setVisible(true);
		}

		@Override
		public void onFailure(Throwable caught) {

		}
	};

	private void loadCodes(String typeId) {
		targetCodeBoxLoader.setVisible(true);
		targetCodeBox.setVisible(false);
		codelistInfoProvider.getCodelistCodes(typeId, codesCallBack);
	}	


	public void setValue(String value) {
		valueLabel.setText(value);
		valueLabel.setTitle(value);
	}

	public void setValueLoaderVisible(boolean visible) {
		valueLabelLoader.setVisible(visible);
		valueLabel.setVisible(!visible);
	}

	public void setValueVisible(boolean visible) {
		valueRow.setVisible(visible);
	}


	private void setupAttributesPanel() {
		attributesPanel = new AttributesPanel(table, style.textboxError());
		attributesPanel.addValueChangeHandler(new ValueChangeHandler<Void>() {

			@Override
			public void onValueChange(ValueChangeEvent<Void> event) {
				fireChange();
			}
		});
	}

	public void setAttributes(List<UIAttribute> attributes) {
		Attributes.sortByAttributeType(attributes);
		attributesPanel.setAttributes(attributes);
	}

	public List<UIAttribute> getAttributes() {
		return attributesPanel.getAttributes();
	}

	public boolean areAttributesValid() {
		return attributesPanel.areValid();
	}


	public void setReadOnly(boolean readOnly) {

		definitionBox.setEnabled(!readOnly);
		if (readOnly) definitionBox.setStyleName(style.textboxError(), false);

		targetCodeBox.setEnabled(!readOnly);
		if (readOnly) targetCodeBox.setStyleName(style.textboxError(), false);

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
