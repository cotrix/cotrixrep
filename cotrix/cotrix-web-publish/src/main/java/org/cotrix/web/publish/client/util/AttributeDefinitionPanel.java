package org.cotrix.web.publish.client.util;

import java.util.Arrays;

import org.cotrix.web.publish.client.resources.PublishConstants;
import org.cotrix.web.publish.shared.AttributeType;
import org.cotrix.web.share.client.resources.CommonResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Label;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeDefinitionPanel extends Composite {

	private static AttributeDefinitionPanelUiBinder uiBinder = GWT.create(AttributeDefinitionPanelUiBinder.class);

	interface AttributeDefinitionPanelUiBinder extends UiBinder<Widget, AttributeDefinitionPanel> {
	}

	public interface TypeLabelProvider {
		String getLabel(AttributeType type);
	}

	public static final TypeLabelProvider SDMXTypeLabelProvider  = new TypeLabelProvider() {

		@Override
		public String getLabel(AttributeType type) {
			switch (type) {
				case CODE: return "Code";
				case DESCRIPTION: return "Description";
				case ANNOTATION: return "Annotation";
				default: throw new IllegalArgumentException("No label mapping found for attribute type "+type);
			}
		}
	};

	public static final TypeLabelProvider CSVTypeLabelProvider  = new TypeLabelProvider() {

		@Override
		public String getLabel(AttributeType type) {
			switch (type) {
				case CODE: return "Primary code";
				case DESCRIPTION: return "Description";
				case ANNOTATION: return "Annotation";
				default: throw new IllegalArgumentException("No label mapping found for attribute type "+type);
			}
		}
	};

	@UiField ListBox typeList;
	@UiField Label inLabel;
	@UiField ListBox languageList;

	@UiField Style style;

	interface Style extends CssResource {
		String listBoxError();
	}
	
	protected TypeLabelProvider typeLabelProvider;
	

	public AttributeDefinitionPanel(TypeLabelProvider typeLabelProvider) {
		this.typeLabelProvider = typeLabelProvider;
		
		initWidget(uiBinder.createAndBindUi(this));

		typeList.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				updateVisibilities();
			}
		});

		setupTypeList();
		setupLanguageList();
	}

	protected void setupTypeList()
	{
		for (AttributeType type:AttributeType.values()) typeList.addItem(getTypeLabel(type), type.toString());
	}

	protected String getTypeLabel(AttributeType type)
	{
		return typeLabelProvider.getLabel(type);
	}

	protected void setupLanguageList()
	{
		String[] languages = PublishConstants.INSTANCE.languages();
		Arrays.sort(languages);
		for (String language:languages) languageList.addItem(language);
	}

	public AttributeType getType()
	{
		int selectedIndex = typeList.getSelectedIndex();
		if (selectedIndex<0) return null;
		String value = typeList.getValue(selectedIndex);
		return AttributeType.valueOf(value);
	}

	public void setType(AttributeType type)
	{
		String value = type.toString();
		for (int i = 0; i < typeList.getItemCount(); i++) {
			if (typeList.getValue(i).equals(value)) {
				typeList.setSelectedIndex(i);
				updateVisibilities();
				return;
			}
		}
	}

	public String getLanguage()
	{
		if (!languageList.isVisible() || languageList.getSelectedIndex()<0) return null;
		return languageList.getValue(languageList.getSelectedIndex());
	}	

	public void setLanguage(String language)
	{
		if (language == null) return;
		for (int i = 0; i < languageList.getItemCount(); i++) {
			if (languageList.getItemText(i).equals(language)) languageList.setSelectedIndex(i);
		}
	}

	protected void updateVisibilities()
	{
		AttributeType type = getType();
		setLanguagePanelVisibile(type != null && type != AttributeType.CODE);
	}

	protected void setLanguagePanelVisibile(boolean visible)
	{
		inLabel.setVisible(visible);
		languageList.setVisible(visible);
	}	

	public void setErrorStyle(){
		typeList.setStyleName(style.listBoxError());
	}

	public void setNormalStyle(){
		typeList.setStyleName(CommonResources.INSTANCE.css().listBox());
	}

	public void setEnabled(boolean enabled)
	{
		typeList.setEnabled(enabled);
		inLabel.setStyleName(CommonResources.INSTANCE.css().paddedTextDisabled(), !enabled);
		languageList.setEnabled(enabled);
	}

}