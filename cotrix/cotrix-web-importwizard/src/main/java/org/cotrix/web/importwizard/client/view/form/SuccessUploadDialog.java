package org.cotrix.web.importwizard.client.view.form;

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
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class SuccessUploadDialog extends PopupPanel {

	private static SuccessUploadDialogUiBinder uiBinder = GWT
			.create(SuccessUploadDialogUiBinder.class);

	interface SuccessUploadDialogUiBinder extends
			UiBinder<Widget, SuccessUploadDialog> {
	}

	public SuccessUploadDialog() {
		setWidget(uiBinder.createAndBindUi(this));
		this.setModal(true);
		this.setGlassEnabled(true);
		this.setAnimationEnabled(true);
		this.setAutoHideEnabled(true);
		this.center();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				int left = Window.getScrollLeft()+ ((Window.getClientWidth( ) - SuccessUploadDialog.this.getOffsetWidth( )) / 2); 
				int top = Window.getScrollTop()+((Window.getClientHeight( ) - SuccessUploadDialog.this.getOffsetHeight( )) / 4);
				SuccessUploadDialog.this.setPopupPosition(left, top);
			}
		});
	}

}
