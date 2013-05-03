package org.cotrix.web.importwizard.client.view.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class HeaderTypeListBox extends Composite {
	public interface OnHeaderTypeSelectedHandler{
		public void onHeaderTypeSelected(int index);
	}
	private OnHeaderTypeSelectedHandler onHeaderTypeSelectedHandler;
	
	private static HeaderTypeListBoxUiBinder uiBinder = GWT
			.create(HeaderTypeListBoxUiBinder.class);

	interface HeaderTypeListBoxUiBinder extends
			UiBinder<Widget, HeaderTypeListBox> {
	}
	
	private Label label;
	public void setRelatedLabel(Label label){
		this.label = label;
	}
	
	@UiField ListBox listbox;
	@UiField Style style;
	interface Style extends CssResource {
		String listBoxError();
		String listBox();
	}

	@UiHandler("listbox")
	void onChangeHandler(ChangeEvent event) {
		onHeaderTypeSelectedHandler.onHeaderTypeSelected(listbox.getSelectedIndex());
	}
	
	public void setOnHeaderTypeSelectedHandler(OnHeaderTypeSelectedHandler onHeaderTypeSelectedHandler){
		this.onHeaderTypeSelectedHandler = onHeaderTypeSelectedHandler;
	}
	
	public void setStyleError(){
		Window.alert("set list box style");
		this.listbox.setStyleName(style.listBoxError());
	}
	
	public void setStyleNormal(){
		this.listbox.setStyleName(style.listBox());
	}
	
	public String getValue(){
		return  (listbox.getSelectedIndex()!=0)?listbox.getItemText(listbox.getSelectedIndex()):null;
	}
	public HeaderTypeListBox() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
