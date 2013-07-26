package org.cotrix.web.importwizard.client.step.csvmapping;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.importwizard.client.util.AlertDialog;
import org.cotrix.web.importwizard.shared.AttributeDefinition;
import org.cotrix.web.importwizard.shared.AttributeType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MappingStepViewImpl extends Composite implements MappingStepView {

	@UiTemplate("MappingStep.ui.xml")
	interface HeaderTypeStepUiBinder extends UiBinder<Widget, MappingStepViewImpl> {}
	private static HeaderTypeStepUiBinder uiBinder = GWT.create(HeaderTypeStepUiBinder.class);

	@UiField FlexTable columnsTable;
	@UiField Style style;
	
	private AlertDialog alertDialog;
	
	interface Style extends CssResource {
		String headerlabel();
		String cell();
	}

	protected List<ColumnDefinitionPanel> columnPanels = new ArrayList<ColumnDefinitionPanel>();
	protected List<AttributeDefinition> columnDefinitions = new ArrayList<AttributeDefinition>();

	public MappingStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setColumns(List<AttributeDefinition> columns)
	{
		columnsTable.removeAllRows();
		columnPanels.clear();
		columnDefinitions.clear();
		
		for (AttributeDefinition column:columns) {
			int row = columnsTable.getRowCount();
			Label label = new Label(column.getName());
			label.setStyleName(style.headerlabel());
			columnsTable.setWidget(row, 0, label);
			columnsTable.getCellFormatter().setStyleName(row, 0, style.cell());
			
			ColumnDefinitionPanel definitionPanel = new ColumnDefinitionPanel();
			definitionPanel.setColumnType(column.getType());
			definitionPanel.setLanguage(column.getLanguage());
			columnPanels.add(definitionPanel);
			
			columnsTable.setWidget(row, 1, definitionPanel);
			//columnsTable.getCellFormatter().setWidth(row, 1, "100px");
			
			columnDefinitions.add(column);
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
	
	public List<AttributeDefinition> getColumns()
	{
		for (int i = 0; i < columnDefinitions.size(); i++) {
			AttributeDefinition definition = columnDefinitions.get(i);
			ColumnDefinitionPanel panel = columnPanels.get(i);
			definition.setType(panel.getColumnType());
			definition.setLanguage(panel.getLanguage());
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
