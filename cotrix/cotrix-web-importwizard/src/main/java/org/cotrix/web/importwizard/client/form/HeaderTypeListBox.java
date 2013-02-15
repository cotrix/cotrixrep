package org.cotrix.web.importwizard.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class HeaderTypeListBox extends Composite {
	
	private static HeaderTypeListBoxUiBinder uiBinder = GWT
			.create(HeaderTypeListBoxUiBinder.class);

	interface HeaderTypeListBoxUiBinder extends
			UiBinder<Widget, HeaderTypeListBox> {
	}
	
	private Label label;
	public void setRelatedLabel(Label label){
		this.label = label;
	}
	
	@UiField
	ListBox listbox;
	
	@UiHandler("listbox")
	void whateverName(ChangeEvent event) {
		int index  = listbox.getSelectedIndex();
		switch (index) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			
			break;
		case 4:
			break;
		case 5:
			break;
		}
	}

	public HeaderTypeListBox() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
