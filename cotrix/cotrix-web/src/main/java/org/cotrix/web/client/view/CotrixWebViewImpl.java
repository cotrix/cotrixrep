package org.cotrix.web.client.view;

import org.cotrix.web.client.presenter.CotrixWebPresenter;
import org.cotrix.web.importwizard.client.step.csvpreview.CsvPreviewStepPresenterImpl;
import org.cotrix.web.menu.client.presenter.MenuPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class CotrixWebViewImpl extends Composite implements CotrixWebView {

	@UiTemplate("CotrixWebPanel.ui.xml")
	interface CotrixMainPanelUiBinder extends UiBinder<Widget, CotrixWebViewImpl> {}
	private static CotrixMainPanelUiBinder uiBinder = GWT.create(CotrixMainPanelUiBinder.class);
	
	@UiField FlowPanel menu;
	@UiField DeckLayoutPanel body;
	
	private Presenter<CotrixWebPresenter> presenter;
	
	public CotrixWebViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setPresenter(CotrixWebPresenter presenter) {
		this.presenter = presenter;
	}

	public void setMenu(MenuPresenter menuPresenter) {
		menuPresenter.go(menu);
	}
	public DeckLayoutPanel getBody() {
		return this.body;
	}

	public void showMenu(int menuIndex) {
		this.body.showWidget(menuIndex);
	}
	


}
