package org.cotrix.web.importwizard.client;

import org.cotrix.web.importwizard.client.ProgressbarLine.Style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ProgressbarButton extends Composite {

	private static ProgressBarButtonUiBinder uiBinder = GWT
			.create(ProgressBarButtonUiBinder.class);

	interface ProgressBarButtonUiBinder extends
			UiBinder<Widget, ProgressbarButton> {
	}
	
	@UiField
	Label button;
	
	@UiField
    Style style;
	
	interface Style extends CssResource {
        String active();
        String inactive();
    }

	public ProgressbarButton(String text) {
		initWidget(uiBinder.createAndBindUi(this));
		button.setText(text);
	}

	public void setActive(boolean isActive){
		if(isActive){
			button.setStyleName(style.active());
		}else{
			button.setStyleName(style.inactive());
		}
	}
}
