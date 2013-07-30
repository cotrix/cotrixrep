package org.cotrix.web.importwizard.client.step.csvmapping;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.importwizard.client.util.AlertDialog;
import org.cotrix.web.importwizard.shared.AttributeDefinition;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.AttributeType;

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

	protected List<ColumnDefinitionPanel> columnPanels = new ArrayList<ColumnDefinitionPanel>();
	protected List<AttributeMapping> columnDefinitions = new ArrayList<AttributeMapping>();

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
		columnDefinitions.clear();

		for (AttributeMapping attributeMapping:mapping) {
			int row = columnsTable.getRowCount();
			Label label = new Label(attributeMapping.getField().getLabel());
			label.setStyleName(style.headerlabel());
			columnsTable.setWidget(row, 0, label);
			columnsTable.getCellFormatter().setStyleName(row, 0, style.cell());

			ColumnDefinitionPanel definitionPanel = new ColumnDefinitionPanel();
			AttributeDefinition attributeDefinition = attributeMapping.getAttributeDefinition();
			if (attributeDefinition!=null) {
				definitionPanel.setColumnType(attributeDefinition.getType());
				definitionPanel.setLanguage(attributeDefinition.getLanguage());
			}
			columnPanels.add(definitionPanel);

			columnsTable.setWidget(row, 1, definitionPanel);
			//columnsTable.getCellFormatter().setWidth(row, 1, "100px");

			columnDefinitions.add(attributeMapping);
		}
	}

	public void setCodeTypeError()
	{
		for (ColumnDefinitionPanel definitionPanel:columnPanels) {
			if (definitionPanel.getColumnType() == AttributeType.CODE) definitionPanel.setErrorStyle();
		}
	}

	public void cleanStyle()
	{
		for (ColumnDefinitionPanel definitionPanel:columnPanels) definitionPanel.setNormalStyle();
	}

	public List<AttributeMapping> getMapping()
	{
		for (int i = 0; i < columnDefinitions.size(); i++) {
			AttributeMapping definition = columnDefinitions.get(i);
			ColumnDefinitionPanel panel = columnPanels.get(i);
			AttributeType attributeType =  panel.getColumnType();
			if (attributeType == null) {
				definition.setAttributeDefinition(null);
			} else {
				AttributeDefinition attributeDefinition = new AttributeDefinition();
				attributeDefinition.setName(definition.getField().getLabel());
				attributeDefinition.setType(panel.getColumnType());
				attributeDefinition.setLanguage(panel.getLanguage());
			}
		}

		return columnDefinitions;
	}

	public void alert(String message) {
		if(alertDialog == null){
			alertDialog = new AlertDialog();
		}
		alertDialog.setMessage(message);
		alertDialog.show();
	}

}
