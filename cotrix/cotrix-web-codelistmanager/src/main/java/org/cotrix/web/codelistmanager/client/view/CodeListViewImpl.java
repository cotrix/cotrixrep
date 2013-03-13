package org.cotrix.web.codelistmanager.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class CodeListViewImpl extends Composite implements CodeListView {

	private static CodeListViewUiBinder uiBinder = GWT.create(CodeListViewUiBinder.class);

	@UiTemplate("CodeListView.ui.xml")
	interface CodeListViewUiBinder extends UiBinder<Widget, CodeListViewImpl> {}

	private Presenter presenter;
	
	public CodeListViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setPresenter( Presenter presenter) {
		this.presenter  = presenter;
	}

	public void init() {
		
	}

}
