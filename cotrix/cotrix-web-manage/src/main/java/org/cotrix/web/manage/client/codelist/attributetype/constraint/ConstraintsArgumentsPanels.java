/**
 * 
 */
package org.cotrix.web.manage.client.codelist.attributetype.constraint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.widgets.EditableLabel;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.shared.GWT;
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
public class ConstraintsArgumentsPanels extends Composite implements HasValueChangeHandlers<List<String>> {
	
	private MetaConstraintProvider metaConstraintProvider = GWT.create(MetaConstraintProvider.class);
	private StackPanel mainpanel;
	private Map<String, ArgumentsPanel> constraintToPanel;
	private ArgumentsPanel currentArgumentsPanel;
	
	public ConstraintsArgumentsPanels() {
		mainpanel = new StackPanel();
		mainpanel.setWidth("100%");
		initWidget(mainpanel);
		constraintToPanel = new HashMap<String, ArgumentsPanel>();
	
		for (MetaConstraint metaConstraint:metaConstraintProvider.getMetaConstraints()) {
			createPanel(metaConstraint);
		}
	}
	
	public int showConstraintPanel(String constraintName) {
		ArgumentsPanel argumentsPanel = constraintToPanel.get(constraintName);
		if (argumentsPanel == null) throw new IllegalArgumentException("Unknown constraint "+constraintName);
		int index = mainpanel.getWidgetIndex(argumentsPanel);
		mainpanel.showStack(index);
		currentArgumentsPanel = argumentsPanel;
		return currentArgumentsPanel.getArgumentsCount();
	}
	
	public List<String> getArgumentsValues() {
		return currentArgumentsPanel.getArgumentsValues();
	}
	
	public void setArgumentsValues(List<String> arguments) {
		currentArgumentsPanel.setArgumentsValues(arguments);
	}
	
	private void createPanel(MetaConstraint metaConstraint) {
		
		ArgumentsPanel argumentsPanel = new ArgumentsPanel();
		for (String name: metaConstraint.getArguments()) argumentsPanel.addArgumentPanel(new ArgumentPanel(name));
		
		mainpanel.add(argumentsPanel);
		
		constraintToPanel.put(metaConstraint.getName(), argumentsPanel);
		
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
		for (ArgumentsPanel argumentsPanel:constraintToPanel.values()) argumentsPanel.setReadOnly(readOnly);
	}
	
	public void setLabelStyle(String styleName) {
		for (ArgumentsPanel argumentsPanel:constraintToPanel.values()) argumentsPanel.setLabelStyle(styleName);
	}
	
	public void setEditorStyle(String styleName) {
		for (ArgumentsPanel argumentsPanel:constraintToPanel.values()) argumentsPanel.setEditorStyle(styleName);
	}
	
	public void setStyle(String style, boolean add) {
		for (ArgumentsPanel argumentsPanel:constraintToPanel.values()) argumentsPanel.setStyle(style, add);
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
		
		private int getArgumentsCount() {
			return argumentPanels.size();
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
			label.setStyleName(CotrixManagerResources.INSTANCE.propertyGrid().argumentLabel());

			panel.add(label);
			textBox = new TextBox();
			textBox.setStyleName(CommonResources.INSTANCE.css().textBox()+" "+CotrixManagerResources.INSTANCE.css().editor());
			textBox.setHeight("31px");
			textBox.setValue(value);
			
			editableLabel = new EditableLabel();
			editableLabel.addEditor(textBox);
			editableLabel.setLabelStyle(CotrixManagerResources.INSTANCE.propertyGrid().argumentValue());
			
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
