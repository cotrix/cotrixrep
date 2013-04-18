package org.cotrix.web.publish.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ChanelPropertyDialogViewImpl extends PopupPanel implements ChanelPropertyDialogView {

	private static ChanelPropertyViewUiBinder uiBinder = GWT
			.create(ChanelPropertyViewUiBinder.class);
	@UiTemplate("ChanelPropertyDialogView.ui.xml")
	interface ChanelPropertyViewUiBinder extends
			UiBinder<Widget, ChanelPropertyDialogViewImpl> {
	}
	private Presenter presenter;
	
	@UiField TextBox url;
	@UiField TextBox username;
	@UiField TextBox password;
	
	@UiHandler("cancelButton")
	void onCancelButtonClicked(ClickEvent event){
		this.hide();
	}
	
	@UiHandler("doneButton")
	public void onDoneButtonClicked(ClickEvent event){
		presenter.onDoneButtonClicked(url.getText(),username.getText(),password.getText());
	}
	
	public ChanelPropertyDialogViewImpl() {
		setWidget(uiBinder.createAndBindUi(this));
		this.setModal(true);
		this.setGlassEnabled(true);
		this.setAnimationEnabled(true);
		this.setAutoHideEnabled(true);
		this.center();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				int left = Window.getScrollLeft()+ ((Window.getClientWidth( ) - ChanelPropertyDialogViewImpl.this.getOffsetWidth( )) / 2); 
				int top = Window.getScrollTop()+((Window.getClientHeight( ) - ChanelPropertyDialogViewImpl.this.getOffsetHeight( )) / 4);
				ChanelPropertyDialogViewImpl.this.setPopupPosition(left, top);
			}
		});
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	


}
