package org.cotrix.web.codelistmanager.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListManagerViewImpl extends Composite implements CodeListManagerView{

	@UiTemplate("CodeListManager.ui.xml")
	interface CodeListManagerUiBinder extends UiBinder<Widget, CodeListManagerViewImpl> {}
	private static CodeListManagerUiBinder uiBinder = GWT.create(CodeListManagerUiBinder.class);

	@UiField HTMLPanel mainPanel;
	@UiField HorizontalPanel flowPanel;
	private Presenter presenter;
	private final String PX = "px";

	public CodeListManagerViewImpl() {
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
