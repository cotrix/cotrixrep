package org.cotrix.web.importwizard.client.progresstracker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ProgressTrackerButton extends Composite {

	private static ProgressTrackerButtonUiBinder uiBinder = GWT
			.create(ProgressTrackerButtonUiBinder.class);

	interface ProgressTrackerButtonUiBinder extends
			UiBinder<Widget, ProgressTrackerButton> {
	}
	
	@UiField
	Label button;
	
	@UiField
    Style style;
	
	interface Style extends CssResource {
        String active();
        String inactive();
    }

	public ProgressTrackerButton(String text) {
		initWidget(uiBinder.createAndBindUi(this));
		button.setText(text);
	}

	public void setActive(boolean isActive){
		button.setStyleName((isActive)?style.active():style.inactive());
	}
}
