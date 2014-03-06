package org.cotrix.web.ingest.client.step.summary;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.ingest.shared.AttributeDefinition;
import org.cotrix.web.ingest.shared.AttributeMapping;
import org.cotrix.web.ingest.shared.MappingMode;

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
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class SummaryStepViewImpl extends ResizeComposite implements SummaryStepView {

	protected static final int FILE_FIELD_ROW = 0;
	protected static final int PROPERTIES_FIELD_ROW = 4;

	@UiTemplate("SummaryStep.ui.xml")
	interface SummaryStepUiBinder extends UiBinder<Widget, SummaryStepViewImpl> {}
	private static SummaryStepUiBinder uiBinder = GWT.create(SummaryStepUiBinder.class);

	@UiField DockLayoutPanel mainPanel;
	@UiField Grid panel;

	@UiField Label fileField;
	@UiField Label codelistField;
	@UiField Label versionField;
	@UiField Label sealedField;
	@UiField FlexTable propertiesTable;
	@UiField HTMLPanel mappingPanel;
	@UiField SimpleCheckBox mappingMode;
	@UiField FlexTable customTable;

	public SummaryStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setMapping(List<AttributeMapping> mappings)
	{
		Log.trace("Setting "+mappings.size()+" mappings");
		
		customTable.removeAllRows();
		int row = 0;
		for (AttributeMapping mapping:mappings) {
			Log.trace("setting "+mapping);
			Log.trace("row "+row);
			StringBuilder mappingDescription = new StringBuilder();
			String originalName = mapping.getField().getLabel();
			
			if (mapping.isMapped()) {
				mappingDescription.append("import <b>").append(originalName).append("</b>");
				AttributeDefinition definition = mapping.getAttributeDefinition();
				if (!originalName.equals(definition.getName())) mappingDescription.append(" as ").append(definition.getName());
				if (definition.getType() !=null) {
					mappingDescription.append(" is ").append(definition.getType().toString());
					if (definition.getLanguage()!=null) mappingDescription.append(" in ").append(definition.getLanguage());
				}
			} else mappingDescription.append("ignore <b>").append(originalName).append("</b>");

			//Log.trace("label "+mappingDescription.toString());
			
			HTML mappingLabel = new HTML(mappingDescription.toString());
			customTable.setWidget(row, 0, mappingLabel);
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
	public void setSealed(boolean sealed)
	{
		//TODO
		sealedField.setText(sealed?"TRUE":"FALSE");
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

	public void setFileName(String fileName)
	{
		fileField.setText(fileName);
	}

	public void setFileNameVisible(boolean visible)
	{
		panel.getRowFormatter().setVisible(FILE_FIELD_ROW, visible);
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
