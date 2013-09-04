package org.cotrix.web.importwizard.client.step.csvmapping;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.importwizard.client.resources.Resources;
import org.cotrix.web.importwizard.client.util.AlertDialog;
import org.cotrix.web.importwizard.client.util.AttributeDefinitionPanel;
import org.cotrix.web.importwizard.shared.AttributeDefinition;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.AttributeType;
import org.cotrix.web.importwizard.shared.Field;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvMappingStepViewImpl extends Composite implements CsvMappingStepView {

	@UiTemplate("CsvMappingStep.ui.xml")
	interface HeaderTypeStepUiBinder extends UiBinder<Widget, CsvMappingStepViewImpl> {}
	private static HeaderTypeStepUiBinder uiBinder = GWT.create(HeaderTypeStepUiBinder.class);
	
	interface Style extends CssResource {
		String cell();
	}
	
	protected static int IGNORE_COLUMN = 0;
	protected static int NAME_COLUMN = 1;
	protected static int LABEL_COLUMN = 2;
	protected static int DEFINITION_COLUMN = 3;

	@UiField TextBox name;
	//@UiField SimpleCheckBox mappingMode;
	@UiField FlexTable columnsTable;
	@UiField Style style;
	
	protected Presenter presenter;

	protected AlertDialog alertDialog;

	protected List<SimpleCheckBox> excludeButtons = new ArrayList<SimpleCheckBox>();
	protected List<TextBox> nameFields = new ArrayList<TextBox>();
	protected List<AttributeDefinitionPanel> columnPanels = new ArrayList<AttributeDefinitionPanel>();
	protected List<Field> fields = new ArrayList<Field>();

	public CsvMappingStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));

		Resources.INSTANCE.css().ensureInjected();
	}
	
	/**
	 * @param presenter the presenter to set
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/*public MappingMode getMappingMode()
	{
		return mappingMode.getValue()?MappingMode.LOG:MappingMode.STRICT;
	}
	
	public void setMappingMode(MappingMode mode)
	{
		mappingMode.setValue(mode==MappingMode.LOG);
	}*/
	
	@Override
	public void setCsvName(String name) {
		this.name.setValue(name);
	}

	@Override
	public String getCsvName() {
		return this.name.getValue();
	}
	
	@UiHandler("reloadButton")
	protected void reload(ClickEvent clickEvent)
	{
		presenter.onReload();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void setMapping(List<AttributeMapping> mapping)
	{
		columnsTable.removeAllRows();
		columnPanels.clear();
		excludeButtons.clear();
		nameFields.clear();
		fields.clear();
		
		FlexCellFormatter cellFormatter = columnsTable.getFlexCellFormatter();

		for (AttributeMapping attributeMapping:mapping) {
			final int row = columnsTable.getRowCount();
			
			final SimpleCheckBox checkBox = new SimpleCheckBox();
			checkBox.setStyleName(Resources.INSTANCE.css().simpleCheckbox());
			checkBox.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					Log.trace("Exclude? "+checkBox.getValue());
					setExclude(row, checkBox.getValue());
				}
			});

			columnsTable.setWidget(row, IGNORE_COLUMN, checkBox);
			excludeButtons.add(checkBox);
			
			Field field = attributeMapping.getField();
			fields.add(field);
		
			TextBox nameField = new TextBox();
			nameField.setStyleName(Resources.INSTANCE.css().textBox());
			nameField.setValue(field.getLabel());
			columnsTable.setWidget(row, NAME_COLUMN, nameField);
			cellFormatter.setStyleName(row, NAME_COLUMN, style.cell());
			nameFields.add(nameField);
			
			columnsTable.setWidget(row, LABEL_COLUMN, new Label("is a"));
			cellFormatter.setStyleName(row, LABEL_COLUMN, Resources.INSTANCE.css().paddedText());

			AttributeDefinitionPanel definitionPanel = new AttributeDefinitionPanel(AttributeDefinitionPanel.CSVTypeLabelProvider);
			columnPanels.add(definitionPanel);
			columnsTable.setWidget(row, DEFINITION_COLUMN, definitionPanel);
			AttributeDefinition attributeDefinition = attributeMapping.getAttributeDefinition();
			if (attributeDefinition == null) {
				checkBox.setValue(true);
			} else {
				checkBox.setValue(false);
				definitionPanel.setType(attributeDefinition.getType());
				definitionPanel.setLanguage(attributeDefinition.getLanguage());
			}
			cellFormatter.setStyleName(row, DEFINITION_COLUMN, style.cell());
		}
	}
	
	protected void setExclude(int row, boolean exclude)
	{
		((TextBox)columnsTable.getWidget(row, NAME_COLUMN)).setEnabled(!exclude);
		((Label)columnsTable.getWidget(row, LABEL_COLUMN)).setStyleName(Resources.INSTANCE.css().paddedTextDisabled(), exclude);
		((AttributeDefinitionPanel)columnsTable.getWidget(row, DEFINITION_COLUMN)).setEnabled(!exclude);
	}
	

	public void setCodeTypeError()
	{
		for (AttributeDefinitionPanel definitionPanel:columnPanels) {
			AttributeType attributeType = definitionPanel.getType();
			if (attributeType!=null && attributeType == AttributeType.CODE) definitionPanel.setErrorStyle();
		}
	}

	public void cleanStyle()
	{
		for (AttributeDefinitionPanel definitionPanel:columnPanels) definitionPanel.setNormalStyle();
	}

	public List<AttributeMapping> getMappings()
	{
		List<AttributeMapping> mappings = new ArrayList<AttributeMapping>();
		for (int i = 0; i < columnPanels.size(); i++) {
			Field field = fields.get(i);
			AttributeDefinition attributeDefinition = getDefinition(i);
			
			AttributeMapping mapping = new AttributeMapping();
			mapping.setField(field);
			mapping.setAttributeDefinition(attributeDefinition);
			mappings.add(mapping);
		}

		return mappings;
	}
	
	protected AttributeDefinition getDefinition(int index)
	{
		if (excludeButtons.get(index).getValue()) return null;
		AttributeDefinitionPanel panel = columnPanels.get(index);
		AttributeType type = panel.getType();
		String language = panel.getLanguage();
		AttributeDefinition attributeDefinition = new AttributeDefinition();
		attributeDefinition.setType(type);
		attributeDefinition.setLanguage(language);

		String name = nameFields.get(index).getValue();
		attributeDefinition.setName(name);
		
		return attributeDefinition;
	}
	
	public void alert(String message) {
		if(alertDialog == null){
			alertDialog = new AlertDialog();
		}
		alertDialog.setMessage(message);
		alertDialog.show();
	}

}
