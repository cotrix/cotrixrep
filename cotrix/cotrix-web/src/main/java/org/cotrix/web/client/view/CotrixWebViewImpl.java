package org.cotrix.web.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CotrixWebViewImpl extends ResizeComposite implements CotrixWebView {

	@UiTemplate("CotrixWebPanel.ui.xml")
	interface CotrixMainPanelUiBinder extends UiBinder<Widget, CotrixWebViewImpl> {}
	private static CotrixMainPanelUiBinder uiBinder = GWT.create(CotrixMainPanelUiBinder.class);
	
	@UiField FlowPanel titleArea;
	@UiField FlowPanel menu;
	@UiField FlowPanel userBar;
	@UiField DeckLayoutPanel body;
	
	protected Presenter presenter;
	
	public CotrixWebViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		titleArea.addDomHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				presenter.onTitleAreaClick();
			}
		}, ClickEvent.getType());
	}
	
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
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
