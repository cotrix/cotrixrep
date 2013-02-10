package org.cotrix.web.importwizard.client;

import org.cotrix.web.importwizard.client.ProgressbarButton.Style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ProgressbarLabel extends Composite  {

	private static ProgressbarLabelUiBinder uiBinder = GWT
			.create(ProgressbarLabelUiBinder.class);

	interface ProgressbarLabelUiBinder extends
			UiBinder<Widget, ProgressbarLabel> {
	}
	@UiField
    Style style;
	
	interface Style extends CssResource {
        String inactive();
    }

	@UiField
	Label label;

	public ProgressbarLabel(String title) {
		initWidget(uiBinder.createAndBindUi(this));
		label.setText(title);
	}
	
	
}
