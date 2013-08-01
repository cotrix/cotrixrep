package org.cotrix.web.importwizard.client.step.metadata;

import java.util.HashMap;
import java.util.Map;

import org.cotrix.web.importwizard.client.util.AlertDialog;
import org.cotrix.web.importwizard.shared.ImportMetadata;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataStepViewImpl extends Composite implements MetadataStepView {

	@UiTemplate("MetadataStep.ui.xml")
	interface MetadataStepUiBinder extends UiBinder<Widget, MetadataStepViewImpl> {}
	private static MetadataStepUiBinder uiBinder = GWT.create(MetadataStepUiBinder.class);

	@UiField DateBox createDate;
	@UiField DateBox updateDate;
	@UiField TextBox name;
	@UiField TextBox fileowner;
	@UiField ListBox version;
	@UiField TextArea description;

	private AlertDialog alertDialog;

	public MetadataStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
		Format dateBoxFormat = new DefaultFormat(dateFormat);
		createDate.setFormat(dateBoxFormat);
		updateDate.setFormat(dateBoxFormat);
	}

	public ImportMetadata getMetadata() {
		ImportMetadata metadata = new ImportMetadata();
		metadata.setName(name.getText());
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("Owner", fileowner.getText());
		attributes.put("Description", description.getText());
		attributes.put("Version", version.getItemText(version.getSelectedIndex()));
		metadata.setAttributes(attributes);
		return metadata;
	}

	public void alert(String message) {
		if(alertDialog == null){
			alertDialog = new AlertDialog();
		}
		alertDialog.setMessage(message);
		alertDialog.show();
	}

	@Override
	public void setMetadata(ImportMetadata metadata) {
		name.setValue(metadata.getName());
		description.setValue(metadata.getAttributes().get("Description"));
		fileowner.setValue(metadata.getAttributes().get("Owner"));
		//createDate.setValue(metadata.getAttributes().get("CreateDate"));
		//updateDate.setValue(metadata.getAttributes().get("UpdateDate"));

		//FIXME version

	}

}
