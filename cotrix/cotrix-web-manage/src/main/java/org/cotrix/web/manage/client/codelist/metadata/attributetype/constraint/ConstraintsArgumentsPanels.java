/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.attributetype.constraint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ConstraintsArgumentsPanels extends Composite implements HasValueChangeHandlers<List<String>> {
	
	private MetaConstraintProvider metaConstraintProvider = GWT.create(MetaConstraintProvider.class);
	private DeckPanel mainpanel;
	private Map<String, ArgumentsPanel> constraintToPanel;
	private ArgumentsPanel currentArgumentsPanel;
	
	public ConstraintsArgumentsPanels() {
		mainpanel = new DeckPanel();
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
		mainpanel.showWidget(index);
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
		
		ArgumentsPanel argumentsPanel = new ArgumentsPanel(metaConstraint.getArguments());
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
	
	public void setStyle(String style, boolean add) {
		for (ArgumentsPanel argumentsPanel:constraintToPanel.values()) argumentsPanel.setStyle(style, add);
	}
	
	private class ArgumentsPanel extends Composite implements HasValueChangeHandlers<List<String>> {

		private Grid argumentsPanel;
		private List<TextBox> textBoxes;
		
		public ArgumentsPanel(List<String> arguments) {
			argumentsPanel = new Grid(arguments.size(), 2);
			argumentsPanel.setWidth("100%");
			
			textBoxes = new ArrayList<TextBox>();
			for (int i = 0; i < arguments.size(); i++) addArgumentPanel(i, arguments.get(i));
			
			initWidget(argumentsPanel);			
		}
		
		public void addArgumentPanel(int row, String argument) {
			
			Label label = new Label(argument);
			label.setStyleName(CotrixManagerResources.INSTANCE.propertyGrid().argumentLabel());
			argumentsPanel.setWidget(row, 0, label);
			argumentsPanel.getCellFormatter().setWidth(row, 0, "80px");
			argumentsPanel.getCellFormatter().setHeight(row, 0, "31px");

			TextBox textBox = new TextBox();
			textBox.setStyleName(CotrixManagerResources.INSTANCE.detailsPanelStyle().textbox());
			argumentsPanel.setWidget(row, 1, textBox);
			
			argumentsPanel.getCellFormatter().getElement(row, 1).getStyle().setPaddingLeft(3, Unit.PX);
			argumentsPanel.getCellFormatter().getElement(row, 1).getStyle().setPaddingRight(3, Unit.PX);

			textBoxes.add(textBox);
			
			textBox.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					Log.trace("Argument changed");
					fireValueChanged();
				}
			});
		}
		
		public List<String> getArgumentsValues() {
			List<String> arguments = new ArrayList<String>();
			for (TextBox textBox:textBoxes) arguments.add(textBox.getValue());
			return arguments;
		}
		
		public void setArgumentsValues(List<String> values) {
			for (int i = 0; i < Math.min(values.size(),textBoxes.size()); i++) {
				textBoxes.get(i).setValue(values.get(i));
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
			for (TextBox textBox:textBoxes) textBox.setEnabled(!readOnly);
		}
		
		public void setStyle(String style, boolean add) {
			for (TextBox textBox:textBoxes) textBox.setStyleName(style, add);
		}
		
		private int getArgumentsCount() {
			return textBoxes.size();
		}
		
	}
}
