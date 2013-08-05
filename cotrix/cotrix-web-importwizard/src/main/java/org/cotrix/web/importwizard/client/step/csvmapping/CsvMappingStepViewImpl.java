package org.cotrix.web.importwizard.client.step.csvmapping;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.importwizard.client.util.AlertDialog;
import org.cotrix.web.importwizard.client.util.AttributeDefinitionPanel;
import org.cotrix.web.importwizard.shared.AttributeDefinition;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.AttributeType;
import org.cotrix.web.importwizard.shared.Field;
import org.cotrix.web.importwizard.shared.MappingMode;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
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
	
	protected static int IGNORE_COLUMN = 0;
	protected static int NAME_COLUMN = 1;
	protected static int LABEL_COLUMN = 2;
	protected static int DEFINITION_COLUMN = 3;

	@UiField TextBox name;
	@UiField ListBox mappingMode;
	@UiField FlexTable columnsTable;
	@UiField Style style;

	private AlertDialog alertDialog;

	interface Style extends CssResource {
		String headerlabel();
		String cell();
		String textPadding();
		String label();
	}

	protected List<CheckBox> ignoreCheckboxes = new ArrayList<CheckBox>();
	protected List<TextBox> nameFields = new ArrayList<TextBox>();
	protected List<AttributeDefinitionPanel> columnPanels = new ArrayList<AttributeDefinitionPanel>();
	protected List<Field> fields = new ArrayList<Field>();

	public CsvMappingStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		setupMappingMode();
	}
	
	protected void setupMappingMode()
	{
		for (MappingMode mode:MappingMode.values()) mappingMode.addItem(getMappingModeLabel(mode), mode.toString());
		mappingMode.setSelectedIndex(0);
	}
	
	protected String getMappingModeLabel(MappingMode mode)
	{
		switch (mode) {
			case IGNORE: return "Ignore";
			case LOG: return "Log";
			case STRICT: return "Strict";
			default: throw new IllegalArgumentException("Unkown label for mapping mode "+mode);
		}
	}
	
	public MappingMode getMappingMode()
	{
		int selectedIndex = mappingMode.getSelectedIndex();
		if (selectedIndex<0) return null;
		String value = mappingMode.getValue(selectedIndex);
		return MappingMode.valueOf(value);
	}
	
	public void setMappingMode(MappingMode mode)
	{
		String value = mode.toString();
		for (int i = 0; i < mappingMode.getItemCount(); i++) {
			if (mappingMode.getValue(i).equals(value)) {
				mappingMode.setSelectedIndex(i);
				return;
			}
		}
	}
	

	@Override
	public void setCsvName(String name) {
		this.name.setValue(name);
	}

	@Override
	public String getCsvName() {
		return this.name.getValue();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void setMapping(List<AttributeMapping> mapping)
	{
		columnsTable.removeAllRows();
		columnPanels.clear();
		ignoreCheckboxes.clear();
		nameFields.clear();
		fields.clear();
		
		addHeader();
		
		FlexCellFormatter cellFormatter = columnsTable.getFlexCellFormatter();

		for (AttributeMapping attributeMapping:mapping) {
			final int row = columnsTable.getRowCount();
			
			final CheckBox ignoreCheckBox = new CheckBox();
			ignoreCheckBox.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					setIgnored(row, ignoreCheckBox.getValue());
				}
			});
			columnsTable.setWidget(row, IGNORE_COLUMN, ignoreCheckBox);
			ignoreCheckboxes.add(ignoreCheckBox);
			
			Field field = attributeMapping.getField();
			fields.add(field);
		
			TextBox nameField = new TextBox();
			nameField.setValue(field.getLabel());
			nameField.setStyleName(style.headerlabel());
			columnsTable.setWidget(row, NAME_COLUMN, nameField);
			cellFormatter.setStyleName(row, NAME_COLUMN, style.cell());
			nameFields.add(nameField);
			
			columnsTable.setWidget(row, LABEL_COLUMN, new Label("is a"));
			cellFormatter.setStyleName(row, LABEL_COLUMN, style.textPadding());

			AttributeDefinitionPanel definitionPanel = new AttributeDefinitionPanel();
			AttributeDefinition attributeDefinition = attributeMapping.getAttributeDefinition();
			if (attributeDefinition == null) {
				setIgnored(row, true);
				ignoreCheckBox.setValue(true);
			} else {
				definitionPanel.setType(attributeDefinition.getType());
				definitionPanel.setLanguage(attributeDefinition.getLanguage());
			}
			columnPanels.add(definitionPanel);
			columnsTable.setWidget(row, DEFINITION_COLUMN, definitionPanel);
			cellFormatter.setStyleName(row, DEFINITION_COLUMN, style.cell());
		}
	}
	
	protected void addHeader()
	{
		Label csvHeader = new Label("Columns");
		csvHeader.setStyleName(style.label());
		columnsTable.setWidget(0, 0, csvHeader);
		columnsTable.getCellFormatter().setStyleName(0, 0, style.headerlabel());
		columnsTable.getFlexCellFormatter().setColSpan(0, 0, 3);
	}
	
	protected void setIgnored(int row, boolean ignore)
	{
		((TextBox)columnsTable.getWidget(row, NAME_COLUMN)).setEnabled(!ignore);
		((AttributeDefinitionPanel)columnsTable.getWidget(row, DEFINITION_COLUMN)).setEnabled(!ignore);
	}
	

	public void setCodeTypeError()
	{
		/*FIXME for (AttributeDefinitionPanel definitionPanel:columnPanels) {
			AttributeDefinition attributeDefinition = definitionPanel.getDefinition();
			if (attributeDefinition!=null && attributeDefinition.getType() == AttributeType.CODE) definitionPanel.setErrorStyle();
		}*/
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
		if (ignoreCheckboxes.get(index).getValue()) return null;
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
