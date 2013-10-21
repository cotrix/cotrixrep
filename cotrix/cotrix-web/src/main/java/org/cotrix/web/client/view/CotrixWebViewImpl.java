package org.cotrix.web.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixWebViewImpl extends ResizeComposite implements CotrixWebView {

	@UiTemplate("CotrixWebPanel.ui.xml")
	interface CotrixMainPanelUiBinder extends UiBinder<Widget, CotrixWebViewImpl> {}
	private static CotrixMainPanelUiBinder uiBinder = GWT.create(CotrixMainPanelUiBinder.class);
	
	@UiField FlowPanel menu;
	@UiField FlowPanel userBar;
	@UiField DeckLayoutPanel body;
	
	public CotrixWebViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public FlowPanel getMenuPanel() {
		return menu;
	}
	
	@Override
	public FlowPanel getUserBarPanel() {
		return userBar;
	}

	public DeckLayoutPanel getModulesPanel() {
		return body;
	}

	public void showModule(int moduleIndex) {
		body.showWidget(moduleIndex);
	}
}
