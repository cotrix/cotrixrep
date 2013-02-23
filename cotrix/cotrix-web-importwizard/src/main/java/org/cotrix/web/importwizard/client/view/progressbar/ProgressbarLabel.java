package org.cotrix.web.importwizard.client.view.progressbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ProgressbarLabel extends Composite {

	private static ProgressbarLabelUiBinder uiBinder = GWT
			.create(ProgressbarLabelUiBinder.class);

	interface ProgressbarLabelUiBinder extends
			UiBinder<Widget, ProgressbarLabel> {
	}

	@UiField
	Style style;

	interface Style extends CssResource {
		String inactive();
		String active();
	}

	@UiField
	Label label;

	public ProgressbarLabel(String title) {
		initWidget(uiBinder.createAndBindUi(this));
		label.setText(title);
	}

	public void setActive(boolean isActive) {
		label.setStyleName((isActive) ? style.active() : style.inactive());
	}

}
