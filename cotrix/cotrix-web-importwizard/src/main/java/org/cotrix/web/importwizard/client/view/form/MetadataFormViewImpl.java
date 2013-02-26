package org.cotrix.web.importwizard.client.view.form;

import java.util.Date;

import org.cotrix.web.importwizard.client.presenter.MetadataFormPresenterImpl;

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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;

public class MetadataFormViewImpl extends Composite implements MetadataFormView<MetadataFormViewImpl> {

	@UiTemplate("MetadataForm.ui.xml")
	interface MetadataFormUiBinder extends UiBinder<Widget, MetadataFormViewImpl> {}
	private static MetadataFormUiBinder uiBinder = GWT.create(MetadataFormUiBinder.class);
	
	@UiField FlowPanel createDate;
	@UiField FlowPanel updateDate;
	@UiField TextArea description;
	
	private Presenter<MetadataFormPresenterImpl> presenter;
	public void setPresenter(Presenter<MetadataFormPresenterImpl> presenter) {
		this.presenter = presenter;
	}
 
	public MetadataFormViewImpl() {
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
