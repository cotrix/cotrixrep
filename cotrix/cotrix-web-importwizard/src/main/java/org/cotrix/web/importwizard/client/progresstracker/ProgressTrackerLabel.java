package org.cotrix.web.importwizard.client.progresstracker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ProgressTrackerLabel extends Composite {

	private static ProgressTrackerLabelUiBinder uiBinder = GWT
			.create(ProgressTrackerLabelUiBinder.class);

	interface ProgressTrackerLabelUiBinder extends
			UiBinder<Widget, ProgressTrackerLabel> {
	}

	@UiField
	Style style;

	interface Style extends CssResource {
		String inactive();
		String active();
	}

	@UiField
	Label label;

	public ProgressTrackerLabel(String title, int labelWidth) {
		initWidget(uiBinder.createAndBindUi(this));
		label.setText(title);
		label.setWidth(labelWidth+"px");
	}

	public void setActive(boolean isActive) {
		label.setStyleName((isActive) ? style.active() : style.inactive());
	}

}
