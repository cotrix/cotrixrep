package org.cotrix.web.codelistmanager.client.view;

import org.cotrix.web.codelistmanager.client.resources.CotrixManagerResources;
import org.cotrix.web.codelistmanager.client.view.CodeListView.Presenter;
import org.cotrix.web.codelistmanager.client.view.CodeListViewImpl.Style;

import com.google.gwt.cell.client.ButtonCellBase.DefaultAppearance.Resources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
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
	
	@UiHandler("nav")
	public void onNavLeftClicked(ClickEvent event) {
		presenter.onNavLeftClicked(this.isShowingNavLeft);
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
		
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}


}
