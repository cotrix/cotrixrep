package org.cotrix.web.importwizard.client.progresstracker;

import org.cotrix.web.importwizard.client.resources.Constants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class ProgressTracker extends Composite {
	
	private int step;
	
	private static ProgressTrackerUiBinder uiBinder = GWT.create(ProgressTrackerUiBinder.class);

	interface ProgressTrackerUiBinder extends UiBinder<Widget, ProgressTracker> {
	}

	public ProgressTracker() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	FlowPanel barPanel;

	@UiField
	FlowPanel textPanel;

	public ProgressTracker(int step, String[] labels) {
		this.step = step;
		initWidget(uiBinder.createAndBindUi(this));
		setUpBarPanel(step);
		setUpTextPanel(labels);
		setActiveIndex(1);
	}

	private void setUpBarPanel(int step) {
		barPanel.setHeight(Constants.BUTTON_WIDTH + "px");
		for (int i = 0; i < step; i++) {
			ProgressTrackerButton button = new ProgressTrackerButton(
					String.valueOf(i + 1));
			barPanel.add((i == 0) ? getFirstLine(false) : getMiddleLine(false));
			barPanel.add(button);
		}
		barPanel.add(getLastLine(false)); // add last line at the end of
	}

	private ProgressTrackerLine getFirstLine(boolean isActive) {
		ProgressTrackerLine line = new ProgressTrackerLine();
		line.setRoundCornerLeft(isActive);
		return line;
	}

	private ProgressTrackerLine getMiddleLine(boolean isActive) {
		return new ProgressTrackerLine();
	}

	private ProgressTrackerLine getLastLine(boolean isActive) {
		ProgressTrackerLine line = new ProgressTrackerLine();
		line.setRoundCornerRight(isActive);
		return line;
	}

	private void setUpTextPanel(String[] labels) {
		textPanel.getElement().getStyle()
				.setProperty("paddingLeft", Constants.LABEL_WIDTH + "px");
		textPanel.setHeight(Constants.BUTTON_WIDTH + "px");
		for (int i = 0; i < labels.length; i++) {
			textPanel.add(new ProgressTrackerLabel(labels[i]));
		}
	}
	
	private void reset(){
		for (int i = 0; i < barPanel.getWidgetCount(); i++) {
			Widget w = barPanel.getWidget(i);
			if (w.getClass() == ProgressTrackerLine.class) {
				ProgressTrackerLine line = (ProgressTrackerLine) w;
				if (i == 0) {
					line.setRoundCornerLeft(false);
				} else {
					line.setActive(false);
				}
			} else {
				ProgressTrackerButton button = (ProgressTrackerButton) w;
				button.setActive(false);
			}
		}
		for (int i = 0; i < textPanel.getWidgetCount(); i++) {
			ProgressTrackerLabel label = (ProgressTrackerLabel) textPanel.getWidget(i);
			label.setActive(false);
		}
	}
	
	public void setActiveIndex(int index) {
		reset();
		
		int targetIndex = (index * 2) - 1;
		for (int i = 0; i <= targetIndex; i++) {
			Widget w = barPanel.getWidget(i);
			if (w.getClass() == ProgressTrackerLine.class) {
				ProgressTrackerLine line = (ProgressTrackerLine) w;
				if (i == 0) {
					line.setRoundCornerLeft(true);
				} else {
					line.setActive(true);
				}
			} else {
				ProgressTrackerButton button = (ProgressTrackerButton) w;
				button.setActive(true);
			}
		}

		// if it is last step
		if (index == step) {
			ProgressTrackerLine line = (ProgressTrackerLine) barPanel
					.getWidget(targetIndex + 1);
			line.setRoundCornerRight(true);
		}

		setLabelActive(index);
	}

	private void setLabelActive(int index) {
		for (int i = 0; i < index; i++) {
			ProgressTrackerLabel label = (ProgressTrackerLabel) textPanel.getWidget(i);
			label.setActive(true);
		}
	}

}
