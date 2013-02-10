package org.cotrix.web.importwizard.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class ProgressbarTracker extends Composite  {
	private int step;
	private FlowPanel barPanel;
	private FlowPanel textPanel;
	public ProgressbarTracker(int step ,String[] labels) {
		this.step = step;
		barPanel = setUpBarPanel(step);
		textPanel = setUpTextPanel(labels);
		
		FlowPanel panel = new FlowPanel();
		panel.add(barPanel);
		panel.add(textPanel);
		
		initWidget(panel);
	}
	
	private FlowPanel setUpBarPanel(int step){
		FlowPanel panel = new FlowPanel();
		panel.setHeight("30px");
		for (int i = 0; i < step; i++) {
			ProgressbarLine line = new ProgressbarLine();
			if(i==0) line.setRoundCornerLeft(false);
			
			ProgressbarButton button = new ProgressbarButton(String.valueOf(i+1));
			panel.add(line);
			panel.add(button);
		}
		ProgressbarLine line = new ProgressbarLine();
		line.setRoundCornerRight(false);
		panel.add(line);
		return panel;
	}
	
	private FlowPanel setUpTextPanel(String[] labels){
		FlowPanel panel = new FlowPanel();
		panel.getElement().getStyle().setProperty("paddingLeft", "50px");
		panel.setHeight("30px");
		for (int i = 0; i < labels.length; i++) {
			ProgressbarLabel l = new ProgressbarLabel(labels[i]);
			panel.add(l);
		}
		return panel;
	}
	
	public void setActiveStep(int index){
		int targetIndex = (index*2) -1 ;
		for (int i = 0; i <= targetIndex; i++) {
			 Widget w = barPanel.getWidget(i);
			 if(w.getClass() == ProgressbarLine.class){
				ProgressbarLine line = (ProgressbarLine) w; 
				if(i == 0 ){
					line.setRoundCornerLeft(true);
				}else{
					line.setActive(true);
				}
			 }else{
				ProgressbarButton button = (ProgressbarButton) w;
				button.setActive(true);
			 }
		}
		if(index == step){
			ProgressbarLine line = (ProgressbarLine) barPanel.getWidget(targetIndex+1);
			line.setRoundCornerRight(true);
		}
	}
}
