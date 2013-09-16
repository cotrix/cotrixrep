package org.cotrix.web.codelistmanager.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListManagerViewImpl extends ResizeComposite implements CodeListManagerView {

	@UiTemplate("CodeListManager.ui.xml")
	interface CodeListManagerUiBinder extends UiBinder<Widget, CodeListManagerViewImpl> {}
	private static CodeListManagerUiBinder uiBinder = GWT.create(CodeListManagerUiBinder.class);

	@UiField SplitLayoutPanel mainPanel;
	@UiField SimpleLayoutPanel westPanel;
	@UiField SimpleLayoutPanel centerPanel;


	public CodeListManagerViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public HasWidgets getWestPanel() {
		return westPanel;
	}
	
	public HasWidgets getCenterPanel() {
		return centerPanel;
	}

	public void showWestPanel(boolean show) {
		mainPanel.setWidgetHidden(westPanel, !show);
	}

}
