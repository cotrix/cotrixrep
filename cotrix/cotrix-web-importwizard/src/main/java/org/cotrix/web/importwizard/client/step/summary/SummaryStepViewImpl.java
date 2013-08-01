package org.cotrix.web.importwizard.client.step.summary;

import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.cotrix.web.importwizard.client.util.ProgressDialog;
import org.cotrix.web.importwizard.shared.AttributeDefinition;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.ImportMetadata;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SummaryStepViewImpl extends Composite implements SummaryStepView {
	
	protected static final int HEADER_ROW = 0;
	protected static final int MAPPING_COLUMN = 0;
	protected static final int METADATA_COLUMN = 1;
	protected static final DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);

	@UiTemplate("SummaryStep.ui.xml")
	interface SummaryStepUiBinder extends UiBinder<Widget, SummaryStepViewImpl> {}
	private static SummaryStepUiBinder uiBinder = GWT.create(SummaryStepUiBinder.class);


	@UiField HTMLPanel panel;
	@UiField FlexTable summaryTable;
	@UiField Style style;

	interface Style extends CssResource {
		String headerlabel();
		String valuelabel();
		String summaryTable();
		String summaryTableHeader();
		String grid();
		String cell();
		String metadata();
		String metadataLabel();
	}

	private ProgressDialog progressDialog;
	
	public SummaryStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		setupHeader();
	}

	protected void setupHeader() {
		summaryTable.setWidget(HEADER_ROW, MAPPING_COLUMN, new HTML("Mappings"));
		summaryTable.setWidget(HEADER_ROW, METADATA_COLUMN, new HTML("Metadata"));
		summaryTable.getCellFormatter().setStyleName(HEADER_ROW, MAPPING_COLUMN, style.summaryTableHeader());
		summaryTable.getCellFormatter().setStyleName(HEADER_ROW, METADATA_COLUMN, style.summaryTableHeader());
	}
	
	public void setMapping(List<AttributeMapping> mappings)
	{
		int row = 1;
		for (AttributeMapping mapping:mappings) {
			StringBuilder mappingDescription = new StringBuilder();
			mappingDescription.append("<b>").append(mapping.getField().getLabel()).append("</b>");
			if (mapping.isMapped()) {
				AttributeDefinition definition = mapping.getAttributeDefinition();
				mappingDescription.append(" mapped as ").append(definition.getType().toString());
				mappingDescription.append(" with name ").append(definition.getName());
				if (definition.getLanguage()!=null) mappingDescription.append(" in ").append(definition.getLanguage());
			} else mappingDescription.append(" ignored");
			
			HTML mappingLabel = new HTML(mappingDescription.toString());
			summaryTable.setWidget(row, MAPPING_COLUMN, mappingLabel);
			row++;
		}
	}
	
	public void setMetadata(ImportMetadata metadata) {

		summaryTable.setWidget(1, METADATA_COLUMN, new HTML("<strong>Name : </strong>"+metadata.getName()));
		
		int row = 2;
		for (Entry<String, String> attribute:metadata.getAttributes().entrySet()) {
			summaryTable.setWidget(row, METADATA_COLUMN, new HTML("<strong>"+attribute.getKey()+" : </strong>"+attribute.getValue()));
			row++;
		}
		
		summaryTable.getFlexCellFormatter().setRowSpan(row, METADATA_COLUMN, summaryTable.getRowCount() - row);
	}
	
	protected String format(Date date)
	{
		if (date == null) return "";
		else return dateFormat.format(date);
	}
	
	public void alert(String message) {

	}

	@Override
	public void showProgress() {
		if(progressDialog == null){
			progressDialog = new ProgressDialog();
		}
		progressDialog.center();
	}

	@Override
	public void hideProgress() {
		if(progressDialog != null) progressDialog.hide();
	}
}
