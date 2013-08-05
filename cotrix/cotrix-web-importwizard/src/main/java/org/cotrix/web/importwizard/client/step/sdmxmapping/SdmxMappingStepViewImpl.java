package org.cotrix.web.importwizard.client.step.sdmxmapping;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.importwizard.client.util.AlertDialog;
import org.cotrix.web.importwizard.shared.AttributeDefinition;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.Field;

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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SdmxMappingStepViewImpl extends Composite implements SdmxMappingStepView {

	@UiTemplate("SdmxMappingStep.ui.xml")
	interface HeaderTypeStepUiBinder extends UiBinder<Widget, SdmxMappingStepViewImpl> {}
	private static HeaderTypeStepUiBinder uiBinder = GWT.create(HeaderTypeStepUiBinder.class);

	protected static int INCLUDE_COLUMN = 0;
	protected static int NAME_COLUMN = 1;

	@UiField FlexTable columnsTable;
	@UiField Style style;

	private AlertDialog alertDialog;

	interface Style extends CssResource {
		String headerlabel();
		String cell();
	}

	protected List<CheckBox> includeCheckboxes = new ArrayList<CheckBox>();
	protected List<TextBox> nameFields = new ArrayList<TextBox>();
	protected List<Field> fields = new ArrayList<Field>();

	public SdmxMappingStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		columnsTable.getColumnFormatter().setWidth(INCLUDE_COLUMN, "20px");
	}

	public void setAttributes(List<AttributeMapping> mappings)
	{
		columnsTable.removeAllRows();
		includeCheckboxes.clear();
		nameFields.clear();
		fields.clear();

		FlexCellFormatter cellFormatter = columnsTable.getFlexCellFormatter();

		for (AttributeMapping attributeMapping:mappings) {
			final int row = columnsTable.getRowCount();

			final CheckBox includeCheckBox = new CheckBox();
			includeCheckBox.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					setIncluded(row, includeCheckBox.getValue());
				}
			});
			columnsTable.setWidget(row, INCLUDE_COLUMN, includeCheckBox);
			includeCheckboxes.add(includeCheckBox);

			Field field = attributeMapping.getField();
			fields.add(field);

			TextBox nameField = new TextBox();
			nameField.setValue(field.getLabel());
			nameField.setStyleName(style.headerlabel());
			columnsTable.setWidget(row, NAME_COLUMN, nameField);
			cellFormatter.setStyleName(row, NAME_COLUMN, style.cell());
			nameFields.add(nameField);
			
			AttributeDefinition attributeDefinition = attributeMapping.getAttributeDefinition();
			if (attributeDefinition == null) {
				includeCheckBox.setValue(false);
				//setIncluded(row, false);
			} else {
				includeCheckBox.setValue(true);
				//setIncluded(row, true);
			}
		}
	}

	protected void setIncluded(int row, boolean include)
	{
		((TextBox)columnsTable.getWidget(row, NAME_COLUMN)).setEnabled(include);
	}
	
	public List<AttributeMapping> getMappings()
	{
		List<AttributeMapping> mappings = new ArrayList<AttributeMapping>();
		for (int i = 0; i < fields.size(); i++) {
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
		if (!includeCheckboxes.get(index).getValue()) return null;

		AttributeDefinition attributeDefinition = new AttributeDefinition();

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
