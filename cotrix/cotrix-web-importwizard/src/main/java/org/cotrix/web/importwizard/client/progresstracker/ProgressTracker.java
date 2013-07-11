package org.cotrix.web.importwizard.client.progresstracker;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.importwizard.client.resources.Constants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class ProgressTracker extends Composite {
	
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
	
	
	protected List<ProgressTrackerButton> buttons = new ArrayList<ProgressTrackerButton>();
	protected List<ProgressTrackerLine> lines = new ArrayList<ProgressTrackerLine>();
	protected ProgressTrackerLine lastLine;
	protected List<ProgressTrackerLabel> labels = new ArrayList<ProgressTrackerLabel>();
	
	public void init(List<String> stepsLabels)
	{
		initButtons(stepsLabels.size());
		initLabels(stepsLabels);
		setCurrentStep(0);
	}
	
	protected void initButtons(int size)
	{
		barPanel.setHeight(Constants.BUTTON_WIDTH + "px");
		for (int i = 0; i < size; i++) {
			ProgressTrackerLine line = new ProgressTrackerLine();
			lines.add(line);
			barPanel.add(line);
			
			ProgressTrackerButton button = new ProgressTrackerButton(String.valueOf(i + 1));
			buttons.add(button);
			barPanel.add(button);
		}
		lastLine = new ProgressTrackerLine();
		barPanel.add(lastLine); // add last line at the end of
	}
	
	protected void initLabels(List<String> stepsLabels) {
		
		textPanel.getElement().getStyle().setProperty("paddingLeft", Constants.LABEL_WIDTH + "px");
		textPanel.setHeight(Constants.BUTTON_WIDTH + "px");
		
		for (String stepLabel:stepsLabels) {
			ProgressTrackerLabel label = new ProgressTrackerLabel(stepLabel);
			labels.add(label);
			textPanel.add(label);
		}
	}
	
	public void setCurrentStep(int stepIndex)
	{
		int stepsCount = buttons.size();
		for (int i = 0; i < stepsCount; i++) {
			buttons.get(i).setActive(i<=stepIndex);
			lines.get(i).setActive(i<=stepIndex);
			labels.get(i).setActive(i<=stepIndex);
		}
		ProgressTrackerLine firstLine = lines.get(0);
		firstLine.setRoundCornerLeft(stepIndex>=0);
		
		lastLine.setActive(stepIndex == buttons.size()-1);
		lastLine.setRoundCornerRight(stepIndex == buttons.size()-1);
	}
	/*
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
	}*/

}
