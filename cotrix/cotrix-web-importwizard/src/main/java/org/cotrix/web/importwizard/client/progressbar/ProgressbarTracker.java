package org.cotrix.web.importwizard.client.progressbar;

import org.cotrix.web.importwizard.client.Constants;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class ProgressbarTracker extends Composite {
	private int step;
	private FlowPanel barPanel;
	private FlowPanel textPanel;

	public ProgressbarTracker(int step, String[] labels) {
		this.step = step;

		barPanel = setUpBarPanel(step); // Create Progress bar indicator.
		textPanel = setUpTextPanel(labels); // Create Text related to each step.

		// Wrap it together.
		FlowPanel panel = new FlowPanel();
		panel.add(barPanel);
		panel.add(textPanel);

		initWidget(panel);
	}

	private FlowPanel setUpBarPanel(int step) {
		FlowPanel panel = new FlowPanel();
		panel.setHeight(Constants.BUTTON_WIDTH + "px");
		for (int i = 0; i < step; i++) {
			ProgressbarButton button = new ProgressbarButton(
					String.valueOf(i + 1));
			panel.add((i == 0) ? getFirstLine(false) : getMiddleLine(false));
			panel.add(button);
		}
		panel.add(getLastLine(false)); // add last line at the end of progress
										// bar
		return panel;
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

	private FlowPanel setUpTextPanel(String[] labels) {
		FlowPanel panel = new FlowPanel();
		panel.getElement().getStyle()
				.setProperty("paddingLeft", Constants.LABEL_WIDTH + "px");
		panel.setHeight(Constants.BUTTON_WIDTH + "px");
		for (int i = 0; i < labels.length; i++) {
			panel.add(new ProgressbarLabel(labels[i]));
		}
		return panel;
	}

	public void setActiveIndex(int index) {
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
