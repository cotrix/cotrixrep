package org.cotrix.web.codelistmanager.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class CodeListManagerViewImpl extends Composite implements CodeListManagerView{

	@UiTemplate("CodeListManager.ui.xml")
	interface CodeListManagerUiBinder extends UiBinder<Widget, CodeListManagerViewImpl> {}
	private static CodeListManagerUiBinder uiBinder = GWT.create(CodeListManagerUiBinder.class);

	@UiField HTMLPanel mainPanel;
	@UiField FlowPanel leftPanel;
	@UiField FlowPanel rightPanel;
	private Presenter presenter;
	private final String PX = "px";

	public CodeListManagerViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	private int getLeftPanelWidth(){
		return 250;
	}
	private int getPanelTopMargin(){
		return 0;
	}
	public void init() {
		leftPanel.setWidth(getLeftPanelWidth()+PX);
		leftPanel.setHeight((Window.getClientHeight() - getPanelTopMargin()) + PX);
		rightPanel.setWidth((Window.getClientWidth() - getLeftPanelWidth()) + PX);
		rightPanel.setHeight((Window.getClientHeight() - getPanelTopMargin()) + PX);
		mainPanel.setWidth(Window.getClientWidth() + PX);
		mainPanel.setHeight(Window.getClientHeight() + PX);
		Window.addResizeHandler(new ResizeHandler() {
			public void onResize(ResizeEvent event) {
				mainPanel.setWidth(event.getWidth() + PX);
				mainPanel.setHeight(event.getHeight() + PX);
				rightPanel.setWidth((Window.getClientWidth() - getLeftPanelWidth()) + PX);
			}
		});
	}

	public void setLeftPanel(IsWidget leftPanel) {
		this.leftPanel.add(leftPanel);
	}
	

}
