package org.cotrix.web.publish.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class ChanelPropertyViewImpl extends PopupPanel implements ChanelPropertyView {

	private static ChanelPropertyViewUiBinder uiBinder = GWT
			.create(ChanelPropertyViewUiBinder.class);
	@UiTemplate("ChanelPropertyView.ui.xml")
	interface ChanelPropertyViewUiBinder extends
			UiBinder<Widget, ChanelPropertyViewImpl> {
	}
	private Presenter presenter;
	public ChanelPropertyViewImpl() {
		setWidget(uiBinder.createAndBindUi(this));
		this.setModal(true);
		this.setGlassEnabled(true);
		this.setAnimationEnabled(true);
		this.setAutoHideEnabled(true);
		this.center();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				int left = Window.getScrollLeft()+ ((Window.getClientWidth( ) - ChanelPropertyViewImpl.this.getOffsetWidth( )) / 2); 
				int top = Window.getScrollTop()+((Window.getClientHeight( ) - ChanelPropertyViewImpl.this.getOffsetHeight( )) / 4);
				ChanelPropertyViewImpl.this.setPopupPosition(left, top);
			}
		});
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	


}
