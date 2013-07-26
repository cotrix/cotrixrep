package org.cotrix.web.importwizard.client.step.sdmxmapping;

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
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SdmxMappingStepViewImpl extends Composite implements SdmxMappingStepView {

	@UiTemplate("SdmxMappingStep.ui.xml")
	interface HeaderTypeStepUiBinder extends UiBinder<Widget, SdmxMappingStepViewImpl> {}
	private static HeaderTypeStepUiBinder uiBinder = GWT.create(HeaderTypeStepUiBinder.class);

	@UiField FlexTable columnsTable;
	@UiField Style style;

	private AlertDialog alertDialog;

	interface Style extends CssResource {
		String headerlabel();
		String cell();
	}

	protected List<AttributeDefinitionPanel> columnPanels = new ArrayList<AttributeDefinitionPanel>();
	protected List<AttributeMapping> columnDefinitions = new ArrayList<AttributeMapping>();

	public SdmxMappingStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setAttributes(List<AttributeMapping> columns)
	{
		columnsTable.removeAllRows();
		columnPanels.clear();
		columnDefinitions.clear();

		for (AttributeMapping column:columns) {
			int row = columnsTable.getRowCount();
			Label label = new Label(column.getField().getLabel());
			label.setStyleName(style.headerlabel());
			columnsTable.setWidget(row, 0, label);
			columnsTable.getCellFormatter().setStyleName(row, 0, style.cell());

			AttributeDefinitionPanel definitionPanel = new AttributeDefinitionPanel();

			if (column.isMapped()) {
				definitionPanel.setAttributeType(column.getAttributeDefinition().getType());
				definitionPanel.setLanguage(column.getAttributeDefinition().getLanguage());
			}
			columnPanels.add(definitionPanel);

			columnsTable.setWidget(row, 1, definitionPanel);
			//columnsTable.getCellFormatter().setWidth(row, 1, "100px");

			columnDefinitions.add(column);
		}
	}

	public void setCodeTypeError()
	{
		for (AttributeDefinitionPanel definitionPanel:columnPanels) {
			if (definitionPanel.getAttributeType() == AttributeType.CODE) definitionPanel.setErrorStyle();
		}
	}

	public void cleanStyle()
	{
		for (AttributeDefinitionPanel definitionPanel:columnPanels) definitionPanel.setNormalStyle();
	}

	public List<AttributeMapping> getAttributes()
	{
		for (int i = 0; i < columnDefinitions.size(); i++) {
			AttributeMapping definition = columnDefinitions.get(i);
			AttributeDefinitionPanel panel = columnPanels.get(i);
			/*definition.setType(panel.getAttributeType());
			definition.setLanguage(panel.getLanguage());*/
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
