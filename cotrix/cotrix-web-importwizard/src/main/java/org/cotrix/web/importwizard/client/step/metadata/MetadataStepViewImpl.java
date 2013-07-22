package org.cotrix.web.importwizard.client.step.metadata;

import org.cotrix.web.importwizard.client.util.AlertDialog;
import org.cotrix.web.share.shared.Metadata;

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
	private Presenter presenter;
	public MetadataStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
		Format dateBoxFormat = new DefaultFormat(dateFormat);
		createDate.setFormat(dateBoxFormat);
		updateDate.setFormat(dateBoxFormat);
	}
	
	public void setPresenter(MetadataStepPresenterImpl presenter) {
		this.presenter = presenter;
	}
	
	public Metadata getMetadata() {
		Metadata metadata = new Metadata();
		metadata.setName(name.getText());
		metadata.setOwner(fileowner.getText());
		metadata.setDescription(description.getText());
		metadata.setVersion(version.getItemText(version.getSelectedIndex()));
		return metadata;
	}

	public boolean isValidated() {
		boolean isValidated = true;
		if(name.getText().length() == 0){
			presenter.alert("Name is required");
			return false;
		}
		if(fileowner.getText().length() == 0){
			presenter.alert("Ower is required");
			return false;
		}
		return isValidated;
	}
	
	public void alert(String message) {
		if(alertDialog == null){
			alertDialog = new AlertDialog();
		}
		alertDialog.setMessage(message);
		alertDialog.show();
	}

}
