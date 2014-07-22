/**
 * 
 */
package org.cotrix.web.publish.client.util;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.publish.client.resources.Resources;
import org.cotrix.web.publish.shared.DefinitionMapping;
import org.cotrix.web.publish.shared.DefinitionMapping.MappingTarget;
import org.cotrix.web.publish.shared.UIDefinition;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
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
public class DefinitionsMappingPanel<T extends MappingTarget> extends Composite {

	public interface DefinitionWidgetProvider<T extends MappingTarget> {
		Widget getTargetWidget(T mapping);
		void include(Widget widget, boolean include);
		T getTarget(Widget widget);
	}
	
	public interface DefinitionMappingsStyle extends CssResource {
		String table();
		String headerCell();
		String emptyCell();
		
		String definitionHeader();
		String targetHeader();
		
		String arrow();
		String checkbox();
	}

	private static int DEFINITION_COLUMN = 0;
	private static int ARROW_COLUMN = 1;
	private static int TARGET_COLUMN = 2;
	private static int INCLUDE_COLUMN = 3;
	
	private DefinitionMappingsStyle style = Resources.INSTANCE.definitionsMapping();

	private SimplePanel container;
	private FlexTable table;
	private FlexTable loadingContainter;

	private List<SimpleCheckBox> includeCheckBoxes = new ArrayList<SimpleCheckBox>();
	private List<Widget> targetWidgets = new ArrayList<Widget>();
	private List<DefinitionPanel> definitionsPanels = new ArrayList<DefinitionPanel>();
	private List<UIDefinition> definitions = new ArrayList<UIDefinition>();

	private DefinitionWidgetProvider<T> widgetProvider;
	private String targetsLabel;
	private String definitionsLabel;
	private boolean includeTargetColumn;
	

	public DefinitionsMappingPanel(DefinitionWidgetProvider<T> widgetProvider, String definitionsLabel, String targetsLabel, boolean includeTargetColumn)
	{
		this.widgetProvider = widgetProvider;
		this.definitionsLabel = definitionsLabel;
		this.targetsLabel = targetsLabel;
		this.includeTargetColumn = includeTargetColumn;
		container = new SimplePanel();
		table = new FlexTable();
		table.setCellPadding(5);
		table.setCellSpacing(5);
		table.setStyleName(style.table());
		setupLoadingContainer();

		container.setWidget(table);
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
		container.setWidget(table);
	}

	/** 
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void setMappings(List<DefinitionMapping> mappings)
	{
		table.removeAllRows();
		definitionsPanels.clear();
		includeCheckBoxes.clear();
		targetWidgets.clear();
		definitions.clear();

		setupTableHeaders(includeTargetColumn);
		
		table.getColumnFormatter().setWidth(DEFINITION_COLUMN, "100%");
		//table.getColumnFormatter().setWidth(ARROW_COLUMN, "70px");

		if (mappings.isEmpty()) {
			Label noAttributesLabel = new Label("No attributes or links");
			table.setWidget(1, 0, noAttributesLabel);
			table.getFlexCellFormatter().setColSpan(1, 0, includeTargetColumn?3:4);
			table.getFlexCellFormatter().setStyleName(1, 0, style.emptyCell());

		} else {
			FlexCellFormatter cellFormatter = table.getFlexCellFormatter();
			for (DefinitionMapping mapping:mappings) {

				final int row = table.getRowCount();
				UIDefinition definition = mapping.getDefinition();
				definitions.add(definition);

				DefinitionPanel definitionPanel = new DefinitionPanel();
				definitionPanel.setDefinition(definition);
				definitionPanel.setEnabled(mapping.isMapped());

				definitionsPanels.add(definitionPanel);
				table.setWidget(row, DEFINITION_COLUMN, definitionPanel);
				cellFormatter.setVerticalAlignment(row, DEFINITION_COLUMN, HasVerticalAlignment.ALIGN_MIDDLE);
				
				final SimpleCheckBox includeCheckBox = new SimpleCheckBox();
				includeCheckBox.setStyleName(CommonResources.INSTANCE.css().simpleCheckbox() + " "+ style.checkbox());
				includeCheckBox.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						setInclude(row, includeCheckBox.getValue());
					}
				});

				includeCheckBox.setValue(mapping.isMapped());
				table.setWidget(row, INCLUDE_COLUMN, includeCheckBox);
				cellFormatter.setVerticalAlignment(row, INCLUDE_COLUMN, HasVerticalAlignment.ALIGN_MIDDLE);
				includeCheckBoxes.add(includeCheckBox);

				Widget targetWidget = widgetProvider.getTargetWidget((T)mapping.getTarget());

				if (targetWidget!=null) {
					
					Image arrow = new Image(Resources.INSTANCE.arrow());
					arrow.setStyleName(style.arrow());
					table.setWidget(row, ARROW_COLUMN, arrow);
					cellFormatter.setVerticalAlignment(row, ARROW_COLUMN, HasVerticalAlignment.ALIGN_MIDDLE);
					//table.getCellFormatter().setWidth(row, ARROW_COLUMN, "60px");
					table.getCellFormatter().setHeight(row, ARROW_COLUMN, "40px");

					widgetProvider.include(targetWidget, mapping.isMapped());

					table.setWidget(row, TARGET_COLUMN, targetWidget);
					cellFormatter.setVerticalAlignment(row, TARGET_COLUMN, HasVerticalAlignment.ALIGN_MIDDLE);
					targetWidgets.add(targetWidget);
				}
				
				
			}
		}
	}

	private void setupTableHeaders(boolean includeMappingColumn)
	{
		Label defsLabel = new Label(definitionsLabel);
		defsLabel.setStyleName(style.definitionHeader());
		table.setWidget(0, 0, defsLabel);
		table.getFlexCellFormatter().setColSpan(0, 0, includeMappingColumn?2:4);
		table.getFlexCellFormatter().setStyleName(0, 0, style.headerCell());

		if (includeMappingColumn) {
			Label tarsLabel = new Label(targetsLabel);
			tarsLabel.setStyleName(style.targetHeader());
			table.setWidget(0, 1, tarsLabel);
			table.getFlexCellFormatter().setColSpan(0, 1, 2);
			table.getFlexCellFormatter().setStyleName(0, 1, style.headerCell());
		}
	}

	private void setInclude(int row, boolean include)
	{
		if (includeTargetColumn) {
			((Image)table.getWidget(row, ARROW_COLUMN)).setResource(include?Resources.INSTANCE.arrow():Resources.INSTANCE.arrowDisabled());
			widgetProvider.include(table.getWidget(row, TARGET_COLUMN), include);
		}
		((DefinitionPanel)table.getWidget(row, DEFINITION_COLUMN)).setEnabled(include);

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
		//for (DefinitionPanel definitionPanel:definitionsPanels) definitionPanel.setNormalStyle();
	}

	public List<DefinitionMapping> getMappings()
	{
		List<DefinitionMapping> mappings = new ArrayList<DefinitionMapping>();
		for (int i = 0; i < definitions.size(); i++) {
			UIDefinition definition = definitions.get(i);
			DefinitionMapping mapping = new DefinitionMapping();

			boolean mapped = includeCheckBoxes.get(i).getValue();
			mapping.setMapped(mapped);
			Widget targetWidget = targetWidgets.isEmpty()?null:targetWidgets.get(i);
			T target = widgetProvider.getTarget(targetWidget);
			mapping.setTarget(target);
			mapping.setDefinition(definition);
			
			mappings.add(mapping);
		}

		return mappings;
	}

}
