package org.cotrix.web.publish.client.wizard.step.summary;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.shared.Language;
import org.cotrix.web.publish.shared.AttributeDefinition;
import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.publish.shared.AttributeMapping.Mapping;
import org.cotrix.web.publish.shared.AttributesMappings;
import org.cotrix.web.publish.shared.MappingMode;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class SummaryStepViewImpl extends ResizeComposite implements SummaryStepView {

	protected static final int PROPERTIES_FIELD_ROW = 3;
	protected static final int CODELIST_MAPPINGS_FIELD_ROW = 4;

	@UiTemplate("SummaryStep.ui.xml")
	interface SummaryStepUiBinder extends UiBinder<Widget, SummaryStepViewImpl> {}
	private static SummaryStepUiBinder uiBinder = GWT.create(SummaryStepUiBinder.class);

	@UiField ScrollPanel summaryScrollPanel;
	
	@UiField DockLayoutPanel mainPanel;
	@UiField Grid panel;

	@UiField Label codelistField;
	@UiField Label versionField;
	@UiField Label stateField;
	@UiField FlexTable propertiesTable;
	@UiField HTMLPanel mappingPanel;
	@UiField SimpleCheckBox mappingMode;
	@UiField FlexTable codelistMappingsTable;
	@UiField FlexTable codesMappingsTable;

	public SummaryStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			summaryScrollPanel.scrollToTop();
			summaryScrollPanel.scrollToLeft();
		}
	}	
	
	public void setMapping(AttributesMappings mappings)
	{
		setMapping(mappings.getCodelistAttributesMapping(), codelistMappingsTable);
		panel.getRowFormatter().setVisible(CODELIST_MAPPINGS_FIELD_ROW, !mappings.getCodelistAttributesMapping().isEmpty());
		
		setMapping(mappings.getCodesAttributesMapping(), codesMappingsTable);
	}

	private void setMapping(List<AttributeMapping> mappings, FlexTable targetTable)
	{
		Log.trace("Setting "+mappings.size()+" mappings");

		targetTable.removeAllRows();
		int row = 0;
		for (AttributeMapping attributeMapping:mappings) {
			Log.trace("setting "+attributeMapping);
			Log.trace("row "+row);
			StringBuilder mappingDescription = new StringBuilder();

			AttributeDefinition definition = attributeMapping.getAttributeDefinition();

			if (attributeMapping.isMapped()) {

				mappingDescription.append("map [<span style=\"font-weight: 44;\">").append(definition.getName().getLocalPart()).append("</span>");

				mappingDescription.append(",").append(definition.getType().getLocalPart());
				if (definition.getLanguage()!=null && definition.getLanguage()!=Language.NONE) mappingDescription.append(",").append(definition.getLanguage().getName());
				mappingDescription.append("]");
				Mapping mapping = attributeMapping.getMapping();
				if (mapping!=null) mappingDescription.append(" to <span style=\"color: #097bfb;\">").append(mapping.getLabel()).append("</span>");
			} else mappingDescription.append("ignore <b>").append(definition.getName().getLocalPart()).append("</b>");

			//Log.trace("label "+mappingDescription.toString());

			HTML mappingLabel = new HTML(mappingDescription.toString());
			targetTable.setWidget(row, 0, mappingLabel);
			row++;
		}
	}

	@Override
	public void setCodelistName(String name) {
		codelistField.setText(name);
	}

	@Override
	public void setCodelistVersion(String version) {
		versionField.setText(version);
	}

	@Override
	public void setState(String state)
	{
		stateField.setText(state);
	}

	public void setMetadataAttributes(Map<String, String> properties){

		propertiesTable.removeAllRows();

		if (properties.size() == 0) {
			panel.getRowFormatter().setVisible(PROPERTIES_FIELD_ROW, false);
		} else {
			panel.getRowFormatter().setVisible(PROPERTIES_FIELD_ROW, true);
			propertiesTable.setText(0, 0, "Name");
			propertiesTable.setText(0, 1, "Value");
			propertiesTable.getCellFormatter().setStyleName(0, 0, CommonResources.INSTANCE.css().propertiesTableHeader());
			propertiesTable.getCellFormatter().setStyleName(0, 1, CommonResources.INSTANCE.css().propertiesTableHeader());
			int row = 1;
			for (Entry<String, String> attribute:properties.entrySet()) {
				propertiesTable.setText(row, 0, attribute.getKey());
				propertiesTable.setText(row, 1, attribute.getValue());
				row++;
			}
		}
	}

	public MappingMode getMappingMode()
	{
		return mappingMode.getValue()?MappingMode.STRICT:MappingMode.LOG;
	}

	public void setMappingMode(MappingMode mode)
	{
		mappingMode.setValue(mode==MappingMode.STRICT);
	}

	public void setMappingModeVisible(boolean visible)
	{
		mainPanel.setWidgetHidden(mappingPanel, !visible);
	}
}
