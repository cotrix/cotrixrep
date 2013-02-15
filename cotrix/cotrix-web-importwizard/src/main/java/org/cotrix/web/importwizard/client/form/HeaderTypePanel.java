package org.cotrix.web.importwizard.client.form;

import org.cotrix.web.importwizard.client.form.DescribeHeaderForm.Style;
import org.cotrix.web.importwizard.client.form.HeaderTypeListBox.OnHeaderTypeSelectedHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class HeaderTypePanel extends Composite implements OnHeaderTypeSelectedHandler {

	private static HeaderTypePanelUiBinder uiBinder = GWT
			.create(HeaderTypePanelUiBinder.class);

	interface HeaderTypePanelUiBinder extends UiBinder<Widget, HeaderTypePanel> {
	}

	@UiField
	HorizontalPanel panel;
	
	@UiField
	Style style;

	interface Style extends CssResource {
		public String textPadding();
	}
	
	public HeaderTypePanel() {
		initWidget(uiBinder.createAndBindUi(this));
		initPanel();
	}

	private void initPanel() {
		HeaderTypeListBox listBox = new HeaderTypeListBox();
		listBox.setOnHeaderTypeSelectedHandler(this);
		panel.add(listBox);
	}
	private void clearSecondWidget(){
		if(panel.getWidgetCount() == 2){
			panel.remove(1);
		}
	}
	public void onHeaderTypeSelected(int index) {
		switch (index) {
		case 0:
			clearSecondWidget();
			break;
		case 1:
			clearSecondWidget();
			break;
		case 2:
			clearSecondWidget();
			break;
		case 3:
			clearSecondWidget();
			Label label = new Label("in");
			label.setStyleName(style.textPadding());
			
			ListBox l = new ListBox();
			l.insertItem("English", "en", 0);
			l.insertItem("thai", "th", 1);
			l.insertItem("German", "de", 2);
			l.insertItem("Spainish", "es", 3);
			
			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.add(label);
			hPanel.add(l);
			
			panel.add(hPanel);
			break;
		case 4:
			clearSecondWidget();
			break;
		case 5:
			clearSecondWidget();
			break;
		}
	}
	

}
