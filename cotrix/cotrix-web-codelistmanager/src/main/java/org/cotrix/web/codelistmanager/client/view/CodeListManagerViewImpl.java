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
	private int getLeftPanelPadding(){
		return 10;
	}
	private int getRightPanelPadding(){
		return 20;
	}

	public void init() {
		leftPanel.setWidth(getLeftPanelWidth() - getLeftPanelPadding()+PX);
		leftPanel.setHeight((Window.getClientHeight() - getPanelTopMargin() - getLeftPanelPadding()) + PX);
		rightPanel.setWidth((Window.getClientWidth() - getLeftPanelWidth() - getRightPanelPadding()) + PX);
		rightPanel.setHeight((Window.getClientHeight() - getPanelTopMargin() - getLeftPanelPadding()) + PX);
		mainPanel.setWidth(Window.getClientWidth() + PX);
		mainPanel.setHeight(Window.getClientHeight() + PX);
		Window.addResizeHandler(new ResizeHandler() {
			public void onResize(ResizeEvent event) {
				mainPanel.setWidth(event.getWidth() + PX);
				mainPanel.setHeight(event.getHeight() + PX);
				rightPanel.setWidth((Window.getClientWidth() - getLeftPanelWidth() - getRightPanelPadding()) + PX);
			}
		});
	}

	public HasWidgets getLeftPanel() {
		return this.leftPanel;
	}

	public HasWidgets getRightPanel() {
		return this.rightPanel;
	}
	
	public void showLeftPanel(boolean isShow){
		this.leftPanel.setVisible(!isShow);
	}
	public void expandRightPanel(boolean isExpand){
		if(isExpand){
			this.rightPanel.setWidth((Window.getClientWidth() - getRightPanelPadding()) + PX);
		}else{
			this.rightPanel.setWidth((Window.getClientWidth() - getLeftPanelWidth() - getRightPanelPadding()) + PX);
		}
	}

}
