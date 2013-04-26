package org.cotrix.web.publish.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class ProgressbarDialog extends PopupPanel  {

	private static ProgressbarDialogUiBinder uiBinder = GWT
			.create(ProgressbarDialogUiBinder.class);

	interface ProgressbarDialogUiBinder extends
			UiBinder<Widget, ProgressbarDialog> {
	}
	@UiField HTMLPanel panel;
	
	public ProgressbarDialog() {
		setWidget(uiBinder.createAndBindUi(this));
		this.setModal(true);
		this.setGlassEnabled(true);
		this.setAnimationEnabled(true);
		this.setAutoHideEnabled(true);
		this.center();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				int left = Window.getScrollLeft()+ ((Window.getClientWidth( ) - ProgressbarDialog.this.getOffsetWidth( )) / 2); 
				int top = Window.getScrollTop()+((Window.getClientHeight( ) - ProgressbarDialog.this.getOffsetHeight( )) / 4);
				ProgressbarDialog.this.setPopupPosition(left, top);
			}
		});
		
	}
	public void finish(){
		panel.clear();
		
		String txt = "<div style=\"width:100%;text-align:center;font-size:20px;\">Publishing Success !!!</div>";
		HTML message = new HTML();
		message.setHTML(txt);
		
		panel.add(message);
	}
}
