/**
 * 
 */
package org.cotrix.web.manage.client.codelist.linktype;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import org.cotrix.web.common.client.widgets.EditableLabel;
import org.cotrix.web.common.shared.codelist.linktype.UIValueFunction.Function;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FunctionsArgumentsPanels extends Composite implements HasValueChangeHandlers<List<String>> {
	
	private StackPanel mainpanel;
	private EnumMap<Function, ArgumentsPanel> functionToPanel;
	private ArgumentsPanel currentArgumentsPanel;
	
	public FunctionsArgumentsPanels() {
		mainpanel = new StackPanel();
		mainpanel.setWidth("100%");
		initWidget(mainpanel);
		functionToPanel = new EnumMap<Function, ArgumentsPanel>(Function.class);
	
		createPanel(Function.IDENTITY);
		createPanel(Function.CUSTOM);
		createPanel(Function.LOWERCASE);
		createPanel(Function.PREFIX);
		createPanel(Function.SUFFIX);
		createPanel(Function.UPPERCASE);
		
		showFunctionPanel(Function.IDENTITY);
	}
	
	public void showFunctionPanel(Function function) {
		ArgumentsPanel argumentsPanel = functionToPanel.get(function);
		if (argumentsPanel == null) throw new IllegalArgumentException("Unknown function "+function);
		int index = mainpanel.getWidgetIndex(argumentsPanel);
		mainpanel.showStack(index);
		currentArgumentsPanel = argumentsPanel;
	}
	
	public List<String> getArgumentsValues() {
		return currentArgumentsPanel.getArgumentsValues();
	}
	
	public void setArgumentsValues(List<String> arguments) {
		currentArgumentsPanel.setArgumentsValues(arguments);
	}
	
	private void createPanel(Function function) {
		
		ArgumentsPanel argumentsPanel = new ArgumentsPanel();
		for (String name: function.getArguments()) argumentsPanel.addArgumentPanel(new ArgumentPanel(name));
		
		mainpanel.add(argumentsPanel);
		
		functionToPanel.put(function, argumentsPanel);
		
		argumentsPanel.addValueChangeHandler(new ValueChangeHandler<List<String>>() {

			@Override
			public void onValueChange(ValueChangeEvent<List<String>> event) {
				fireValueChanged();
			}
		});
	}
	
	private void fireValueChanged() {
		Log.trace("Arguments changed");
		ValueChangeEvent.fire(this, getArgumentsValues());
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<String>> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}
	
	public void setReadOnly(boolean readOnly) {
		for (ArgumentsPanel argumentsPanel:functionToPanel.values()) argumentsPanel.setReadOnly(readOnly);
	}
	
	public void setLabelStyle(String styleName) {
		for (ArgumentsPanel argumentsPanel:functionToPanel.values()) argumentsPanel.setLabelStyle(styleName);
	}
	
	public void setEditorStyle(String styleName) {
		for (ArgumentsPanel argumentsPanel:functionToPanel.values()) argumentsPanel.setEditorStyle(styleName);
	}
	
	public void setStyle(String style, boolean add) {
		for (ArgumentsPanel argumentsPanel:functionToPanel.values()) argumentsPanel.setStyle(style, add);
	}
	
	private class ArgumentsPanel extends Composite implements HasValueChangeHandlers<List<String>> {

		private VerticalPanel argumentsPanel;
		private List<ArgumentPanel> argumentPanels = new ArrayList<ArgumentPanel>();
		
		public ArgumentsPanel() {
			argumentsPanel = new VerticalPanel();
			argumentsPanel.setWidth("100%");
			initWidget(argumentsPanel);
		}
		
		public void addArgumentPanel(ArgumentPanel argumentPanel) {
			argumentsPanel.add(argumentPanel);
			argumentPanels.add(argumentPanel);
			argumentPanel.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					Log.trace("Argument changed");
					fireValueChanged();
				}
			});
		}
		
		public List<String> getArgumentsValues() {
			List<String> arguments = new ArrayList<String>();
			for (ArgumentPanel argumentPanel:argumentPanels) arguments.add(argumentPanel.getText());
			return arguments;
		}
		
		public void setArgumentsValues(List<String> values) {
			for (int i = 0; i < Math.min(values.size(),argumentPanels.size()); i++) {
				argumentPanels.get(i).setText(values.get(i));
			}
		}
		
		private void fireValueChanged() {
			ValueChangeEvent.fire(this, getArgumentsValues());
		}

		@Override
		public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<String>> handler) {
			return addHandler(handler, ValueChangeEvent.getType());
		}

		public void setReadOnly(boolean readOnly) {
			for (ArgumentPanel argumentPanel:argumentPanels) argumentPanel.setReadOnly(readOnly);
		}
		
		public void setLabelStyle(String styleName) {
			for (ArgumentPanel argumentPanel:argumentPanels) argumentPanel.setLabelStyle(styleName);
		}
		
		public void setEditorStyle(String styleName) {
			for (ArgumentPanel argumentPanel:argumentPanels) argumentPanel.setEditorStyle(styleName);
		}
		
		public void setStyle(String style, boolean add) {
			for (ArgumentPanel argumentPanel:argumentPanels) argumentPanel.setStyle(style, add);
		}
		
	}
	
	private class ArgumentPanel extends Composite implements HasText, HasValueChangeHandlers<String> {
		
		private EditableLabel editableLabel;
		private TextBox textBox;
		
		public ArgumentPanel(String name) {
			this(name, null);
		}
		
		public ArgumentPanel(String name, String value) {
			VerticalPanel panel = new VerticalPanel();
			panel.setWidth("100%");
			Label label = new Label(name);
			label.getElement().getStyle().setPaddingLeft(5, Unit.PX);
			label.getElement().getStyle().setPaddingTop(3, Unit.PX);
			label.getElement().getStyle().setPaddingBottom(4, Unit.PX);

			panel.add(label);
			textBox = new TextBox();
			textBox.setValue(value);
			
			editableLabel = new EditableLabel();
			editableLabel.addEditor(textBox);
			
			textBox.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					editableLabel.setText(event.getValue());
				}
			});
			
			panel.add(editableLabel);
			initWidget(panel);
		}

		@Override
		public String getText() {
			return textBox.getValue();
		}

		@Override
		public void setText(String text) {
			textBox.setValue(text);	
			editableLabel.setText(text);
		}

		@Override
		public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
			return textBox.addValueChangeHandler(handler);
		}

		public void setReadOnly(boolean readOnly) {
			editableLabel.setReadOnly(readOnly);
		}
		
		public void setLabelStyle(String styleName) {
			editableLabel.setLabelStyle(styleName);
		}
		
		public void setEditorStyle(String styleName) {
			textBox.setStyleName(styleName);
		}
		
		public void setStyle(String style, boolean add) {
			textBox.setStyleName(style, add);
		}
	}
}
