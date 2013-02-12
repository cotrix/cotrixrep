package org.cotrix.web.importwizard.client.form;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;

public class MetadataForm extends Composite {

	private static MetadataFormUiBinder uiBinder = GWT
			.create(MetadataFormUiBinder.class);

	interface MetadataFormUiBinder extends UiBinder<Widget, MetadataForm> {
	}
	
	@UiField
	FlowPanel createDate;

	@UiField
	FlowPanel updateDate;
	
	@UiField
	TextArea description;

	public MetadataForm() {
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
}
