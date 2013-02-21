package org.cotrix.web.importwizard.client.view.progressbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class ProgressbarLine extends Composite  {

	private static ProgressbarTrackerUiBinder uiBinder = GWT
			.create(ProgressbarTrackerUiBinder.class);

	interface ProgressbarTrackerUiBinder extends
	UiBinder<Widget, ProgressbarLine> {
	}

	@UiField
	HTML line;

	@UiField
	Style style;

	interface Style extends CssResource {
		String active();
		String active_round_right();
		String active_round_left();
		String inactive();
		String inactive_round_right();
		String inactive_round_left();
	}

	public ProgressbarLine() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setRoundCornerRight(boolean isActive){
		line.setStyleName((isActive)?style.active_round_right():style.inactive_round_right());
	}

	public void setRoundCornerLeft(boolean isActive){
		line.setStyleName((isActive)?style.active_round_left():style.inactive_round_left());
	}

	public void setActive(boolean isActive){
		line.setStyleName((isActive)?style.active():style.inactive());
	}

}
