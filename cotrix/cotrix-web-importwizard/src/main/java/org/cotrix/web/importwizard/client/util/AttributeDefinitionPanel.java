package org.cotrix.web.importwizard.client.util;

import java.util.Arrays;

import org.cotrix.web.importwizard.client.resources.ImportConstants;
import org.cotrix.web.importwizard.shared.AttributeDefinition;
import org.cotrix.web.importwizard.shared.AttributeType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Label;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeDefinitionPanel extends Composite {
	
	enum Type {
		
		IGNORE("Ignore"),
		CODE("Code"),
		DESCRIPTION("Description");
		
		private String label;

		/**
		 * @param label
		 */
		private Type(String label) {
			this.label = label;
		}

		/**
		 * @return the label
		 */
		public String getLabel() {
			return label;
		}
	}

	private static AttributeDefinitionPanelUiBinder uiBinder = GWT.create(AttributeDefinitionPanelUiBinder.class);

	interface AttributeDefinitionPanelUiBinder extends UiBinder<Widget, AttributeDefinitionPanel> {
	}

	@UiField Label nameLabel;
	@UiField ListBox typeList;
	@UiField TextBox nameField;
	@UiField Label inLabel;
	@UiField ListBox languageList;

	@UiField
	Style style;

	interface Style extends CssResource {
		public String textPadding();
		String listBoxError();
		String listBox();
	}

	public AttributeDefinitionPanel() {
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
		for (Type type:Type.values()) typeList.addItem(type.getLabel(), type.toString());
		setType(Type.IGNORE);
	}
	
	protected void setupLanguageList()
	{
		String[] languages = ImportConstants.INSTANCE.languages();
		Arrays.sort(languages);
		for (String language:languages) languageList.addItem(language);
	}
	
	protected Type getType()
	{
		int selectedIndex = typeList.getSelectedIndex();
		if (selectedIndex<0) return null;
		String value = typeList.getValue(selectedIndex);
		return Type.valueOf(value);
	}
	
	protected void setType(Type type)
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
	
	protected String getLanguage()
	{
		if (!languageList.isVisible() || languageList.getSelectedIndex()<0) return null;
		return languageList.getValue(languageList.getSelectedIndex());
	}	
	
	protected void setLanguage(String language)
	{
		if (language == null) return;
		for (int i = 0; i < languageList.getItemCount(); i++) {
			if (languageList.getItemText(i).equals(language)) languageList.setSelectedIndex(i);
		}
	}
	
	protected void updateVisibilities()
	{
		Type type = getType();
		if (type == null) {
			setNameVisibile(false);
			setLanguageVisibile(false);
		}
		else {
			switch (type) {
				case IGNORE: {
					setLanguageVisibile(false);
					setNameVisibile(false);
				} break;
				case CODE: {
					setNameVisibile(true);
					setLanguageVisibile(false);
				} break;
				case DESCRIPTION: {
					setNameVisibile(true);
					setLanguageVisibile(true);
				} break;
			}
		}
	}
	
	protected void setNameVisibile(boolean visible)
	{
		nameLabel.setVisible(visible);
		nameField.setVisible(visible);
	}	
	
	protected void setLanguageVisibile(boolean visible)
	{
		inLabel.setVisible(visible);
		languageList.setVisible(visible);
	}	

	public void setErrorStyle(){
		typeList.setStyleName(style.listBoxError());
	}
	
	public void setNormalStyle(){
		typeList.setStyleName(style.listBox());
	}
	
	public void setDefinition(AttributeDefinition definition)
	{
		if (definition == null) {
			nameField.setValue("");
			setType(Type.IGNORE);
		} else {
			nameField.setValue(definition.getName());
			switch (definition.getType()) {
				case CODE: setType(Type.CODE); break;
				case DESCRIPTION: setType(Type.DESCRIPTION); break;
			}
			setLanguage(definition.getLanguage());
		}
	}
	
	public AttributeDefinition getDefinition()
	{
		Type type = getType();
		if (type == null || type == Type.IGNORE) return null;
		
		AttributeDefinition definition = new AttributeDefinition();
		definition.setName(nameField.getValue());
		
		switch (type) {
			case CODE: definition.setType(AttributeType.CODE); break;
			case DESCRIPTION: definition.setType(AttributeType.DESCRIPTION); break;
			default: break;
		}
		definition.setLanguage(getLanguage());
		
		return definition;
	}
}
