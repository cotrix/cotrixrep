package org.cotrix.web.importwizard.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class AlertDialog extends PopupPanel {
	private static final Binder binder = GWT.create(Binder.class);
	interface Binder extends UiBinder<Widget, AlertDialog> {}

	@UiField HTML label;

	public AlertDialog() {
		setWidget(binder.createAndBindUi(this));
		this.setModal(true);
		this.setGlassEnabled(true);
		this.setAnimationEnabled(true);
		this.setAutoHideEnabled(true);
		this.center();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				int left = Window.getScrollLeft()+ ((Window.getClientWidth( ) - AlertDialog.this.getOffsetWidth( )) / 2); 
				int top = Window.getScrollTop()+((Window.getClientHeight( ) - AlertDialog.this.getOffsetHeight( )) / 4);
				AlertDialog.this.setPopupPosition(left, top);
			}
		});
	}

	public void setMessage(String message){
		this.label.setHTML(message);
	}

}
