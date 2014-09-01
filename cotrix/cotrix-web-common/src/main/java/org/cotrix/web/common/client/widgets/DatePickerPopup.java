/**
 * 
 */
package org.cotrix.web.common.client.widgets;

import java.util.Date;

import org.cotrix.web.common.client.resources.CommonResources;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DatePickerPopup extends PopupPanel implements HasValueChangeHandlers<Date> {
	
	private DatePicker datePicker;
	
	public DatePickerPopup() {
		CommonResources.INSTANCE.datePicker().ensureInjected();
		
		setAutoHideEnabled(true);
		
		datePicker = new DatePicker();
		add(datePicker);
		
		datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				hide();
			}
		});
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Date> handler) {
		return datePicker.addValueChangeHandler(handler);
	}
	
	

}
