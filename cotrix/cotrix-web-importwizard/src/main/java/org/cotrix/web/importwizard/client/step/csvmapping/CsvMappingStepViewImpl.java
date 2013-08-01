package org.cotrix.web.importwizard.client.step.csvmapping;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.importwizard.client.util.AlertDialog;
import org.cotrix.web.importwizard.client.util.AttributeDefinitionPanel;
import org.cotrix.web.importwizard.shared.AttributeDefinition;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.AttributeType;
import org.cotrix.web.importwizard.shared.Field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvMappingStepViewImpl extends Composite implements CsvMappingStepView {

	@UiTemplate("CsvMappingStep.ui.xml")
	interface HeaderTypeStepUiBinder extends UiBinder<Widget, CsvMappingStepViewImpl> {}
	private static HeaderTypeStepUiBinder uiBinder = GWT.create(HeaderTypeStepUiBinder.class);

	@UiField TextBox name;
	@UiField FlexTable columnsTable;
	@UiField Style style;

	private AlertDialog alertDialog;

	interface Style extends CssResource {
		String headerlabel();
		String cell();
	}

	protected List<AttributeDefinitionPanel> columnPanels = new ArrayList<AttributeDefinitionPanel>();
	protected List<Field> fields = new ArrayList<Field>();

	public CsvMappingStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
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
		fields.clear();
		
		addHeader();

		for (AttributeMapping attributeMapping:mapping) {
			int row = columnsTable.getRowCount();
			Field field = attributeMapping.getField();
			fields.add(field);
			
			Label label = new Label(field.getLabel());
			label.setStyleName(style.headerlabel());
			columnsTable.setWidget(row, 0, label);
			columnsTable.getCellFormatter().setStyleName(row, 0, style.cell());
			
			columnsTable.setWidget(row, 1, new Label("map as"));
			columnsTable.getCellFormatter().setStyleName(row, 1, style.cell());

			AttributeDefinitionPanel definitionPanel = new AttributeDefinitionPanel();
			AttributeDefinition attributeDefinition = attributeMapping.getAttributeDefinition();
			definitionPanel.setDefinition(attributeDefinition);
			columnPanels.add(definitionPanel);

			columnsTable.setWidget(row, 2, definitionPanel);
		}
	}
	
	protected void addHeader()
	{
		Label csvHeader = new Label("CSV columns");
		csvHeader.setStyleName(style.headerlabel());
		columnsTable.setWidget(0, 0, csvHeader);
		columnsTable.getCellFormatter().setStyleName(0, 0, style.headerlabel());
		
		Label cotrixHeader = new Label("Cotrix model");
		cotrixHeader.setStyleName(style.headerlabel());
		columnsTable.setWidget(0, 2, cotrixHeader);
		columnsTable.getCellFormatter().setStyleName(0, 2, style.headerlabel());
	}
	

	public void setCodeTypeError()
	{
		for (AttributeDefinitionPanel definitionPanel:columnPanels) {
			AttributeDefinition attributeDefinition = definitionPanel.getDefinition();
			if (attributeDefinition!=null && attributeDefinition.getType() == AttributeType.CODE) definitionPanel.setErrorStyle();
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
			AttributeDefinitionPanel panel = columnPanels.get(i);
			AttributeDefinition attributeDefinition = panel.getDefinition();
			AttributeMapping mapping = new AttributeMapping();
			mapping.setField(field);
			mapping.setAttributeDefinition(attributeDefinition);
			mappings.add(mapping);
		}

		return mappings;
	}

	public void alert(String message) {
		if(alertDialog == null){
			alertDialog = new AlertDialog();
		}
		alertDialog.setMessage(message);
		alertDialog.show();
	}

}
