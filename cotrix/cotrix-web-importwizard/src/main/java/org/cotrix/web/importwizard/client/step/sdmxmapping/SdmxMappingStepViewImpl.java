package org.cotrix.web.importwizard.client.step.sdmxmapping;

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

	protected List<AttributeDefinitionPanel> attributesPanels = new ArrayList<AttributeDefinitionPanel>();
	protected List<Field> fields = new ArrayList<Field>();

	public SdmxMappingStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setAttributes(List<AttributeMapping> mappings)
	{
		columnsTable.removeAllRows();
		attributesPanels.clear();
		fields.clear();

		for (AttributeMapping mapping:mappings) {
			int row = columnsTable.getRowCount();
			Field field = mapping.getField();
			fields.add(field);
			
			Label label = new Label(mapping.getField().getLabel());
			label.setStyleName(style.headerlabel());
			columnsTable.setWidget(row, 0, label);
			columnsTable.getCellFormatter().setStyleName(row, 0, style.cell());
			
			columnsTable.setWidget(row, 1, new Label("map as"));
			columnsTable.getCellFormatter().setStyleName(row, 1, style.cell());

			AttributeDefinitionPanel definitionPanel = new AttributeDefinitionPanel();
			//FIXME definitionPanel.setDefinition(mapping.getAttributeDefinition());
			attributesPanels.add(definitionPanel);

			columnsTable.setWidget(row, 2, definitionPanel);
		}
	}

	public void setCodeTypeError()
	{
		for (AttributeDefinitionPanel attributePanel:attributesPanels) {
			//FIXME AttributeDefinition attributeDefinition = attributePanel.getDefinition();
			//FIXME if (attributeDefinition!=null && attributeDefinition.getType() == AttributeType.CODE) attributePanel.setErrorStyle();
		}
	}

	public void cleanStyle()
	{
		for (AttributeDefinitionPanel definitionPanel:attributesPanels) definitionPanel.setNormalStyle();
	}

	public List<AttributeMapping> getMappings()
	{
		List<AttributeMapping> mappings = new ArrayList<AttributeMapping>();
		for (int i = 0; i < attributesPanels.size(); i++) {
			Field field = fields.get(i);
			AttributeDefinitionPanel panel = attributesPanels.get(i);
			//FIXME AttributeDefinition attributeDefinition = panel.getDefinition();
			AttributeMapping mapping = new AttributeMapping();
			mapping.setField(field);
			//FIXME mapping.setAttributeDefinition(attributeDefinition);
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
