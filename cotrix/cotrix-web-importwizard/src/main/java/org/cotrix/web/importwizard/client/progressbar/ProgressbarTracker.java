package org.cotrix.web.importwizard.client.progressbar;

import org.cotrix.web.importwizard.client.Constants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class ProgressbarTracker extends Composite {
	private int step;
	private static ProgressbarUiBinder uiBinder = GWT
			.create(ProgressbarUiBinder.class);

	interface ProgressbarUiBinder extends UiBinder<Widget, ProgressbarTracker> {
	}

	public ProgressbarTracker() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	FlowPanel barPanel;

	@UiField
	FlowPanel textPanel;

	public ProgressbarTracker(int step, String[] labels) {
		this.step = step;
		initWidget(uiBinder.createAndBindUi(this));
		setUpBarPanel(step);
		setUpTextPanel(labels);
		setActiveIndex(1);
	}

	private void setUpBarPanel(int step) {
		barPanel.setHeight(Constants.BUTTON_WIDTH + "px");
		for (int i = 0; i < step; i++) {
			ProgressbarButton button = new ProgressbarButton(
					String.valueOf(i + 1));
			barPanel.add((i == 0) ? getFirstLine(false) : getMiddleLine(false));
			barPanel.add(button);
		}
		barPanel.add(getLastLine(false)); // add last line at the end of
											// progress
	}

	private ProgressbarLine getFirstLine(boolean isActive) {
		ProgressbarLine line = new ProgressbarLine();
		line.setRoundCornerLeft(isActive);
		return line;
	}

	private ProgressbarLine getMiddleLine(boolean isActive) {
		return new ProgressbarLine();
	}

	private ProgressbarLine getLastLine(boolean isActive) {
		ProgressbarLine line = new ProgressbarLine();
		line.setRoundCornerRight(isActive);
		return line;
	}

	private void setUpTextPanel(String[] labels) {
		textPanel.getElement().getStyle()
				.setProperty("paddingLeft", Constants.LABEL_WIDTH + "px");
		textPanel.setHeight(Constants.BUTTON_WIDTH + "px");
		for (int i = 0; i < labels.length; i++) {
			textPanel.add(new ProgressbarLabel(labels[i]));
		}
	}
	
	private void reset(){
		for (int i = 0; i < barPanel.getWidgetCount(); i++) {
			Widget w = barPanel.getWidget(i);
			if (w.getClass() == ProgressbarLine.class) {
				ProgressbarLine line = (ProgressbarLine) w;
				if (i == 0) {
					line.setRoundCornerLeft(false);
				} else {
					line.setActive(false);
				}
			} else {
				ProgressbarButton button = (ProgressbarButton) w;
				button.setActive(false);
			}
		}
	}
	
	public void setActiveIndex(int index) {
		reset();
		
		int targetIndex = (index * 2) - 1;
		for (int i = 0; i <= targetIndex; i++) {
			Widget w = barPanel.getWidget(i);
			if (w.getClass() == ProgressbarLine.class) {
				ProgressbarLine line = (ProgressbarLine) w;
				if (i == 0) {
					line.setRoundCornerLeft(true);
				} else {
					line.setActive(true);
				}
			} else {
				ProgressbarButton button = (ProgressbarButton) w;
				button.setActive(true);
			}
		}

		// if it is last step
		if (index == step) {
			ProgressbarLine line = (ProgressbarLine) barPanel
					.getWidget(targetIndex + 1);
			line.setRoundCornerRight(true);
		}

		setLabelActive(index);
	}

	private void setLabelActive(int index) {
		for (int i = 0; i < index; i++) {
			ProgressbarLabel label = (ProgressbarLabel) textPanel.getWidget(i);
			label.setActive(true);
		}
	}

}
