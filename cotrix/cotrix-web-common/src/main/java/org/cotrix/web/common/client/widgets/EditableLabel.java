/**
 * 
 */
package org.cotrix.web.common.client.widgets;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class EditableLabel extends Composite implements ValueChangeHandler<String> {
	
	private StackPanel panel;
	private Label label;
	private int labelIndex;
	private int editorIndex;
	
	public EditableLabel() {
		panel = new StackPanel();
		panel.setWidth("100%");
		panel.setHeight("100%");
		label = new Label();
		panel.add(label);
		initWidget(panel);
		
		labelIndex = panel.getWidgetIndex(label);
	}
	
	@UiChild(limit=1, tagname="editor")
	public void addEditor(Widget editor) {
		panel.add(editor);
		editorIndex = panel.getWidgetIndex(editor);
	}
	
	public void setText(String text) {
		label.setText(text);
	}
	
	public void setLabelStyle(String style) {
		label.setStyleName(style);
	}

	/**
	 * @return the label
	 */
	public Label getLabel() {
		return label;
	}
	
	public void setReadOnly(boolean readOnly) {
		panel.showStack(readOnly?labelIndex:editorIndex);
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		setText(event.getValue());
	}

}
