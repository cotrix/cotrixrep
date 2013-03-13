package org.cotrix.web.codelistmanager.client.view;

import org.cotrix.web.codelistmanager.client.view.CodeListView.Presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class CodeListDetailViewImpl extends Composite implements CodeListDetailView {

	
	@UiTemplate("CodeListDetail.ui.xml")
	interface CodeListDetailUiBinder extends UiBinder<Widget, CodeListDetailViewImpl> {}
	private static CodeListDetailUiBinder uiBinder = GWT.create(CodeListDetailUiBinder.class);

	private Presenter presenter;
	
	public CodeListDetailViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void init() {
		
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}


}
