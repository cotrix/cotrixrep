package org.cotrix.web.importwizard.client.step.metadata;

import java.util.Date;

import org.cotrix.web.importwizard.client.util.AlertDialog;
import org.cotrix.web.share.shared.Metadata;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataStepViewImpl extends Composite implements MetadataStepView {

	@UiTemplate("MetadataStep.ui.xml")
	interface MetadataStepUiBinder extends UiBinder<Widget, MetadataStepViewImpl> {}
	private static MetadataStepUiBinder uiBinder = GWT.create(MetadataStepUiBinder.class);
	
	@UiField FlowPanel createDate;
	@UiField FlowPanel updateDate;
	@UiField TextBox name;
	@UiField TextBox fileowner;
	@UiField ListBox version;
	@UiField TextArea description;
	
	private AlertDialog alertDialog;
	private Presenter presenter;
	public MetadataStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		addDatePicker(createDate);
		addDatePicker(updateDate);
	}
	
	private  void addDatePicker(FlowPanel panel){
		 // Create a basic date picker
	    DatePicker datePicker = new DatePicker();
	    final Label text = new Label();

	    // Set the value in the text box when the user selects a date
	    datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
	      public void onValueChange(ValueChangeEvent<Date> event) {
	        Date date = event.getValue();
	        String dateString = DateTimeFormat.getMediumDateFormat().format(date);
	        text.setText(dateString);
	      }
	    });

	    // Set the default value
	    datePicker.setValue(new Date(), true);

	    // Create a DateBox
	    DateTimeFormat dateFormat = DateTimeFormat.getLongDateFormat();
	    DateBox dateBox = new DateBox();
	    dateBox.setFormat(new DateBox.DefaultFormat(dateFormat));

	    panel.add(dateBox);
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