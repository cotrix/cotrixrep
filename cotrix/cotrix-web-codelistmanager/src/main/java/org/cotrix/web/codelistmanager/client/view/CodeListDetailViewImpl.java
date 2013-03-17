package org.cotrix.web.codelistmanager.client.view;

import org.cotrix.web.codelistmanager.client.resources.CotrixManagerResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class CodeListDetailViewImpl extends Composite implements CodeListDetailView {


	@UiTemplate("CodeListDetail.ui.xml")
	interface CodeListDetailUiBinder extends UiBinder<Widget, CodeListDetailViewImpl> {}
	private static CodeListDetailUiBinder uiBinder = GWT.create(CodeListDetailUiBinder.class);

	CotrixManagerResources resources = GWT.create(CotrixManagerResources.class);

	private Presenter presenter;
	private boolean isShowingNavLeft = true;
	@UiField Image nav;
	@UiField Label codelistName;
	@UiField FlowPanel metadataPanel;
	@UiField FlowPanel gridPanel;

	@UiHandler("nav")
	public void onNavLeftClicked(ClickEvent event) {
		presenter.onNavLeftClicked(this.isShowingNavLeft);
	}
	@UiHandler("codelistName")
	public void onCodelistNameClicked(ClickEvent event) {
		presenter.onCodelistNameClicked(metadataPanel.isVisible());
	}

	@UiField Style style;
	interface Style extends CssResource {
		String nav();
	}

	public CodeListDetailViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}


	public void showNavLeft(){
		nav.setStyleName(style.nav());
		nav.setUrl(resources.nav_collapse_left().getSafeUri().asString());
		isShowingNavLeft = true;
	}

	public void showNavRight(){
		nav.setStyleName(style.nav());
		nav.setUrl(resources.nav_collapse_right().getSafeUri().asString());
		isShowingNavLeft = false;
	}

	public void init() {
//		CwDataGrid grid = new CwDataGrid();
//		gridPanel.add(grid);
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public void expandWidth(boolean isExpand) {
		if(isExpand){

		}else{

		}

	}
	public void showMetadataPanel(boolean isVisible) {
		metadataPanel.setVisible(!isVisible);
	}



}
