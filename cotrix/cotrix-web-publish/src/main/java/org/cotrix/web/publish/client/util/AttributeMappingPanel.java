/**
 * 
 */
package org.cotrix.web.publish.client.util;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.publish.shared.AttributeDefinition;
import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.share.client.resources.CommonResources;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeMappingPanel extends Composite {

	protected static int IGNORE_COLUMN = 0;
	protected static int DEFINITION_COLUMN = 1;
	protected static int NAME_COLUMN = 2;

	protected SimplePanel container;
	protected FlexTable columnsTable;
	protected FlexTable loadingContainter;

	protected boolean typeDefinition;

	protected List<SimpleCheckBox> includeCheckBoxes = new ArrayList<SimpleCheckBox>();
	protected List<TextBox> nameFields = new ArrayList<TextBox>();
	protected List<AttributeDefinitionPanel> definitionsPanels = new ArrayList<AttributeDefinitionPanel>();
	protected List<AttributeDefinition> definitions = new ArrayList<AttributeDefinition>();

	public AttributeMappingPanel(boolean typeDefinition)
	{
		this.typeDefinition = typeDefinition;
		container = new SimplePanel();
		columnsTable = new FlexTable();
		setupLoadingContainer();

		container.setWidget(columnsTable);
		initWidget(container);
	}

	protected void setupLoadingContainer()
	{
		loadingContainter = new FlexTable();
		loadingContainter.getElement().setAttribute("align", "center");
		loadingContainter.setWidget(0, 0, new Label("loading..."));
	}

	public void setLoading()
	{
		container.setWidget(loadingContainter);
	}

	public void unsetLoading()
	{
		container.setWidget(columnsTable);
	}

	/** 
	 * {@inheritDoc}
	 */
	public void setMapping(List<AttributeMapping> mapping)
	{
		columnsTable.removeAllRows();
		definitionsPanels.clear();
		includeCheckBoxes.clear();
		nameFields.clear();
		definitions.clear();

		FlexCellFormatter cellFormatter = columnsTable.getFlexCellFormatter();

		for (AttributeMapping attributeMapping:mapping) {

			final int row = columnsTable.getRowCount();
			AttributeDefinition attributeDefinition = attributeMapping.getAttributeDefinition();
			definitions.add(attributeDefinition);

			final SimpleCheckBox checkBox = new SimpleCheckBox();
			checkBox.setStyleName(CommonResources.INSTANCE.css().simpleCheckbox());
			checkBox.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					Log.trace("Include? "+checkBox.getValue());
					setInclude(row, checkBox.getValue());
				}
			});

			checkBox.setValue(attributeMapping.isMapped());
			columnsTable.setWidget(row, IGNORE_COLUMN, checkBox);
			includeCheckBoxes.add(checkBox);

			if (typeDefinition) {

				AttributeDefinitionPanel definitionPanel = new AttributeDefinitionPanel();

				definitionPanel.setName(attributeDefinition.getName().getLocalPart());
				definitionPanel.setType(attributeDefinition.getType().getLocalPart());
				definitionPanel.setLanguage(attributeDefinition.getLanguage());
				definitionPanel.setEnabled(attributeMapping.isMapped());

				definitionsPanels.add(definitionPanel);
				columnsTable.setWidget(row, DEFINITION_COLUMN, definitionPanel);
				//FIXME cellFormatter.setStyleName(row, DEFINITION_COLUMN, Resources.INSTANCE.css().mappingCell());
			}

			TextBox nameField = new TextBox();
			nameField.setStyleName(CommonResources.INSTANCE.css().textBox());
			nameField.setWidth("200px");
			nameField.setValue(attributeMapping.getColumnName());
			nameField.setEnabled(attributeMapping.isMapped());
			columnsTable.setWidget(row, NAME_COLUMN, nameField);
			//FIXME cellFormatter.setStyleName(row, NAME_COLUMN, Resources.INSTANCE.css().mappingCell());
			nameFields.add(nameField);
		}
	}

	protected void setInclude(int row, boolean include)
	{
		((TextBox)columnsTable.getWidget(row, NAME_COLUMN)).setEnabled(include);
		if (typeDefinition) {
			((AttributeDefinitionPanel)columnsTable.getWidget(row, DEFINITION_COLUMN)).setEnabled(include);
		}
	}

	public void setCodeTypeError()
	{
		/*for (AttributeDefinitionPanel definitionPanel:definitionsPanels) {
			AttributeType attributeType = definitionPanel.getType();
			if (attributeType!=null && attributeType == AttributeType.CODE) definitionPanel.setErrorStyle();
		}*/
	}

	public void cleanStyle()
	{
		for (AttributeDefinitionPanel definitionPanel:definitionsPanels) definitionPanel.setNormalStyle();
	}

	public List<AttributeMapping> getMappings()
	{
		List<AttributeMapping> mappings = new ArrayList<AttributeMapping>();
		for (int i = 0; i < definitions.size(); i++) {
			AttributeDefinition attributeDefinition = definitions.get(i);
			AttributeMapping mapping = new AttributeMapping();

			boolean mapped = includeCheckBoxes.get(i).getValue();
			mapping.setMapped(mapped);
			String name = nameFields.get(i).getValue();
			mapping.setColumnName(name);
			mapping.setAttributeDefinition(attributeDefinition);
			mappings.add(mapping);
		}

		return mappings;
	}

}
