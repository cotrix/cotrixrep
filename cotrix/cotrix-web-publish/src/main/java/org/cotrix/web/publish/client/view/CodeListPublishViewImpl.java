package org.cotrix.web.publish.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CodeListPublishViewImpl extends Composite implements CodeListPublishView{

	@UiTemplate("CodeListPublish.ui.xml")
	interface CodeListManagerUiBinder extends UiBinder<Widget, CodeListPublishViewImpl> {}
	private static CodeListManagerUiBinder uiBinder = GWT.create(CodeListManagerUiBinder.class);

	@UiField HTMLPanel mainPanel;
	@UiField HorizontalPanel flowPanel;
	private Presenter presenter;
	private final String PX = "px";

	public CodeListPublishViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public HasWidgets getContentPanel() {
		return this.flowPanel;
	}

	public void showLeftPanel(boolean isShow) {
		flowPanel.getWidget(0).setVisible(!isShow);
	}

}
