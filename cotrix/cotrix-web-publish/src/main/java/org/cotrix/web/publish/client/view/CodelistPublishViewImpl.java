package org.cotrix.web.publish.client.view;

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

public class CodelistPublishViewImpl extends Composite implements CodelistPublishView {

	@UiTemplate("CodelistPublish.ui.xml")
	interface CodelistPublishUiBinder extends UiBinder<Widget, CodelistPublishViewImpl> {}

	private static CodelistPublishUiBinder uiBinder = GWT.create(CodelistPublishUiBinder.class);

	public CodelistPublishViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	private Presenter presenter;
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	

}
