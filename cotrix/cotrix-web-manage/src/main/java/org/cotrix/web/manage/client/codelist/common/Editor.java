/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common;

import org.cotrix.web.common.client.resources.CommonResources;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.SuggestListBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Editor<E extends IsWidget & TakesValue<String>> extends Composite implements TakesValue<String> {
	
	public static Editor<TextBox> getEditor(TextBox box) {
		final Editor<TextBox> editor = new Editor<TextBox>(box);
		box.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				editor.setLabelValue(event.getValue());
			}
		});
		return editor;
	}
	
	public static Editor<SuggestListBox> getEditor(SuggestListBox box, final SuggestionParser suggestionParser) {
		final Editor<SuggestListBox> editor = new Editor<SuggestListBox>(box);
		box.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				editor.setLabelValue(suggestionParser.parse(event.getSelectedItem()));	
			}
		});
		return editor;
	}
	
	public static Editor<ListBoxAdapter> getEditor(ListBox listBox) {
		
		final ListBoxAdapter adapter = new ListBoxAdapter(listBox);
		
		final Editor<ListBoxAdapter> editor = new Editor<ListBoxAdapter>(adapter);
		
		listBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				editor.setValue(adapter.getValue());
			}
		});
		return editor;
	}
	
	private StackPanel panel;
	private Label label;
	private Image loader;
	private int labelIndex;
	private int loaderIndex;
	private int editorIndex;
	private int lastVisibleItemIndex;
	private E editor;
	
	private Editor(E editorField) {
		panel = new StackPanel();
		panel.setWidth("100%");
		panel.setHeight("100%");
		
		label = new Label();
		panel.add(label);
		labelIndex = panel.getWidgetIndex(label);
		
		loader = new Image(CommonResources.INSTANCE.dataLoader());
		panel.add(loader);
		loaderIndex = panel.getWidgetIndex(loader);
		
		panel.add(editorField);
		editorIndex = panel.getWidgetIndex(editorField);
		
		initWidget(panel);
		
		setReadOnly(true);
	}
	
	public void setLabelStyle(String style) {
		label.setStyleName(style);
	}
	
	public void setReadOnly(boolean readOnly) {
		lastVisibleItemIndex = readOnly?labelIndex:editorIndex;
		panel.showStack(lastVisibleItemIndex);
	}
	
	public void setLoaderVisible(boolean visible) {
		if (!visible) panel.showStack(lastVisibleItemIndex);
		else panel.showStack(loaderIndex);
	}
	
	private void setLabelValue(String value) {
		label.setText(value);
	}

	@Override
	public void setValue(String value) {
		label.setText(value);
		editor.setValue(value);
	}

	@Override
	public String getValue() {
		return editor.getValue();
	}
	
	public static class ListBoxAdapter implements IsWidget, TakesValue<String> {
		
		private ListBox listBox;
		
		public ListBoxAdapter(ListBox listBox) {
			this.listBox = listBox;
		}

		@Override
		public void setValue(String value) {
			for (int i = 0; i<listBox.getItemCount(); i++) {
				if (listBox.getValue(i).equals(value)) {
					listBox.setSelectedIndex(i);
					return;
				}
			}
			throw new IllegalArgumentException("Unknwown item value "+value);
		}

		@Override
		public String getValue() {
			int selectedIndex = listBox.getSelectedIndex();
			return selectedIndex>=0?listBox.getValue(selectedIndex):null;
		}

		@Override
		public Widget asWidget() {
			return listBox;
		}
		
	}
	
	public static interface SuggestionParser {
		public String parse(Suggestion suggestion);
	}

}
