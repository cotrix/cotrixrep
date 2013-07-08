package org.cotrix.web.importwizard.client.step.mapping;

import java.util.ArrayList;

import org.cotrix.web.importwizard.client.step.mapping.HeaderTypeListBox.OnHeaderTypeSelectedHandler;
import org.cotrix.web.share.shared.HeaderType;
import org.cotrix.web.share.shared.HeaderType;
import org.cotrix.web.share.shared.HeaderType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
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
	public HeaderType getHeaderType(){
		HeaderType type = new HeaderType();
		if(panel.getWidgetCount() == 1){
			HeaderTypeListBox listBox = (HeaderTypeListBox) panel.getWidget(0);
			type.setValue(listBox.getValue());
		}else{
			HeaderTypeListBox listBox = (HeaderTypeListBox) panel.getWidget(0);
			type.setValue(listBox.getValue());

			HorizontalPanel hp = (HorizontalPanel) panel.getWidget(1);
			ListBox l = (ListBox) hp.getWidget(1);
			type.setRelatedValue(l.getValue(l.getSelectedIndex()));
		}
		return type;
	}

	private void initPanel() {
		HeaderTypeListBox listBox = new HeaderTypeListBox();
		listBox.setOnHeaderTypeSelectedHandler(this);
		panel.add(listBox);
	}
	public void setStyleError(){
		HeaderTypeListBox listBox = (HeaderTypeListBox) panel.getWidget(0);
		listBox.setStyleError();
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
			Label label = new Label("in");
			label.setStyleName(style.textPadding());

			ListBox l = new ListBox();
			l.insertItem("--------", "text", 0);
			l.insertItem("English", "en", 1);
			l.insertItem("Thai", "th", 2);
			l.insertItem("German", "de", 3);
			l.insertItem("Spanish", "es", 4);

			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.add(label);
			hPanel.add(l);

			panel.add(hPanel);
			break;
		}
	}


}
