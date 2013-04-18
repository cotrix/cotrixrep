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

public class InfoDialog extends PopupPanel {

	private static InfoDialogUiBinder uiBinder = GWT
			.create(InfoDialogUiBinder.class);

	interface InfoDialogUiBinder extends UiBinder<Widget, InfoDialog> {
	}
	
	@UiField HTMLPanel panel;
	
	public InfoDialog(String desc) {
		setWidget(uiBinder.createAndBindUi(this));
		this.setModal(true);
		this.setGlassEnabled(true);
		this.setAnimationEnabled(true);
		this.setAutoHideEnabled(true);
		this.center();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				int left = Window.getScrollLeft()+ ((Window.getClientWidth( ) - InfoDialog.this.getOffsetWidth( )) / 2); 
				int top = Window.getScrollTop()+((Window.getClientHeight( ) - InfoDialog.this.getOffsetHeight( )) / 4);
				InfoDialog.this.setPopupPosition(left, top);
			}
		});
		HTML txt = new HTML(desc);
		this.panel.add(txt);
	}


}
