/**
 * 
 */
package org.cotrix.web.publish.client.util;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.shared.Language;
import org.cotrix.web.publish.client.resources.Resources;
import org.cotrix.web.publish.shared.AttributeDefinition;
import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.publish.shared.AttributeMapping.Mapping;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeMappingPanel<T extends Mapping> extends Composite {
	
	public interface DefinitionWidgetProvider<T extends Mapping> {
		Widget getWidget(T mapping);
		void include(Widget widget, boolean include);
		T getMapping(Widget widget);
	}

	private static int IGNORE_COLUMN = 0;
	private static int DEFINITION_COLUMN = 1;
	private static int IMAGE_COLUMN = 2;
	private static int MAPPING_COLUMN = 3;

	private SimplePanel container;
	private FlexTable columnsTable;
	private FlexTable loadingContainter;

	private List<SimpleCheckBox> includeCheckBoxes = new ArrayList<SimpleCheckBox>();
	private List<Widget> mappingWidgets = new ArrayList<Widget>();
	private List<AttributeDefinitionPanel> definitionsPanels = new ArrayList<AttributeDefinitionPanel>();
	private List<AttributeDefinition> definitions = new ArrayList<AttributeDefinition>();
	
	private DefinitionWidgetProvider<T> widgetProvider;
	private String headerLabel;
	private String attributeLabel;

	public AttributeMappingPanel(DefinitionWidgetProvider<T> widgetProvider, String attributeLabel, String headerLabel)
	{
		this.widgetProvider = widgetProvider;
		this.attributeLabel = attributeLabel;
		this.headerLabel = headerLabel;
		container = new SimplePanel();
		columnsTable = new FlexTable();
		columnsTable.setCellPadding(5);
		columnsTable.setCellSpacing(5);
		columnsTable.setStyleName(Resources.INSTANCE.css().mappingAttributeTable());
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
	@SuppressWarnings("unchecked")
	public void setMapping(List<AttributeMapping> mapping)
	{
		columnsTable.removeAllRows();
		definitionsPanels.clear();
		includeCheckBoxes.clear();
		mappingWidgets.clear();
		definitions.clear();
		
		setupHeader();

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
			cellFormatter.setVerticalAlignment(row, IGNORE_COLUMN, HasVerticalAlignment.ALIGN_MIDDLE);
			includeCheckBoxes.add(checkBox);


				AttributeDefinitionPanel definitionPanel = new AttributeDefinitionPanel();

				definitionPanel.setName(attributeDefinition.getName().getLocalPart());
				definitionPanel.setType(attributeDefinition.getType().getLocalPart());
				if (attributeDefinition.getLanguage() == null || attributeDefinition.getLanguage() == Language.NONE) {
					definitionPanel.setLanguagePanelVisibile(false);
				} else definitionPanel.setLanguage(attributeDefinition.getLanguage());
				definitionPanel.setEnabled(attributeMapping.isMapped());

				definitionsPanels.add(definitionPanel);
				columnsTable.setWidget(row, DEFINITION_COLUMN, definitionPanel);
				cellFormatter.setVerticalAlignment(row, DEFINITION_COLUMN, HasVerticalAlignment.ALIGN_MIDDLE);
			
			
			Image arrow = new Image(Resources.INSTANCE.arrow());
			columnsTable.setWidget(row, IMAGE_COLUMN, arrow);
			cellFormatter.setVerticalAlignment(row, IMAGE_COLUMN, HasVerticalAlignment.ALIGN_MIDDLE);
			columnsTable.getCellFormatter().setWidth(row, IMAGE_COLUMN, "60px");
			columnsTable.getCellFormatter().setHeight(row, IMAGE_COLUMN, "40px");

			Widget widget = widgetProvider.getWidget((T)attributeMapping.getMapping());
			
			widgetProvider.include(widget, attributeMapping.isMapped());
			
			
			columnsTable.setWidget(row, MAPPING_COLUMN, widget);
			cellFormatter.setVerticalAlignment(row, MAPPING_COLUMN, HasVerticalAlignment.ALIGN_MIDDLE);
			mappingWidgets.add(widget);
		}
	}
	
	protected void setupHeader()
	{
		Label attributesLabel = new Label(attributeLabel);
		attributesLabel.setStyleName(Resources.INSTANCE.css().mappingAttributeHeader());
		columnsTable.setWidget(0, 0, attributesLabel);
		columnsTable.getFlexCellFormatter().setColSpan(0, 0, 3);
		columnsTable.getFlexCellFormatter().setStyleName(0, 0, Resources.INSTANCE.css().mappingAttributeHeaderCell());
		
				
		Label columnsLabel = new Label(headerLabel);
		columnsLabel.setStyleName(Resources.INSTANCE.css().mappingAttributeHeader());
		columnsTable.setWidget(0, 1, columnsLabel);
		columnsTable.getFlexCellFormatter().setStyleName(0, 1, Resources.INSTANCE.css().mappingAttributeHeaderCell());
	}

	protected void setInclude(int row, boolean include)
	{
		widgetProvider.include(columnsTable.getWidget(row, MAPPING_COLUMN), include);
		((AttributeDefinitionPanel)columnsTable.getWidget(row, DEFINITION_COLUMN)).setEnabled(include);

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
			Widget widget = mappingWidgets.get(i);
			T mappingValue = widgetProvider.getMapping(widget);
			mapping.setMapping(mappingValue);
			mapping.setAttributeDefinition(attributeDefinition);
			mappings.add(mapping);
		}

		return mappings;
	}

}
