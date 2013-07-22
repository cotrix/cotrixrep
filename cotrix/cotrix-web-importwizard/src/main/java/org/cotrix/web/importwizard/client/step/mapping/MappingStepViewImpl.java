package org.cotrix.web.importwizard.client.step.mapping;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.importwizard.shared.ColumnDefinition;
import org.cotrix.web.importwizard.shared.ColumnType;

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
public class MappingStepViewImpl extends Composite implements MappingStepFormView {

	@UiTemplate("MappingStep.ui.xml")
	interface HeaderTypeStepUiBinder extends UiBinder<Widget, MappingStepViewImpl> {}
	private static HeaderTypeStepUiBinder uiBinder = GWT.create(HeaderTypeStepUiBinder.class);

	@UiField FlexTable columnsTable;
	@UiField Style style;
	
	interface Style extends CssResource {
		String headerlabel();
		String cell();
		String valuelabel();
	}

	protected List<ColumnDefinitionPanel> columnPanels = new ArrayList<ColumnDefinitionPanel>();
	protected List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();

	public MappingStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setColumns(List<ColumnDefinition> columns)
	{
		columnsTable.removeAllRows();
		columnPanels.clear();
		columnDefinitions.clear();
		
		for (ColumnDefinition column:columns) {
			int row = columnsTable.getRowCount();
			Label label = new Label(column.getName());
			label.setStyleName(style.headerlabel());
			
			ColumnDefinitionPanel definitionPanel = new ColumnDefinitionPanel();
			definitionPanel.setColumnType(column.getType());
			definitionPanel.setLanguage(column.getLanguage());
			columnPanels.add(definitionPanel);
			
			columnsTable.getCellFormatter().setStyleName(row, 0, style.cell());
			columnsTable.setWidget(row, 0, label);
			columnsTable.setWidget(row, 1, definitionPanel);
			
			columnDefinitions.add(column);
		}
	}
	
	public void setCodeTypeError()
	{
		for (ColumnDefinitionPanel definitionPanel:columnPanels) {
			if (definitionPanel.getColumnType() == ColumnType.CODE) definitionPanel.setErrorStyle();
		}
	}
	
	public void cleanStyle()
	{
		for (ColumnDefinitionPanel definitionPanel:columnPanels) definitionPanel.setNormalStyle();
	}
	
	public List<ColumnDefinition> getColumns()
	{
		for (int i = 0; i < columnDefinitions.size(); i++) {
			ColumnDefinition definition = columnDefinitions.get(i);
			ColumnDefinitionPanel panel = columnPanels.get(i);
			definition.setType(panel.getColumnType());
			definition.setLanguage(panel.getLanguage());
		}
		
		return columnDefinitions;
	}

}
