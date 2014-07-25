/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.linkdefinition;

import java.util.List;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition;
import org.cotrix.web.common.shared.codelist.linkdefinition.UIValueFunction;
import org.cotrix.web.common.shared.codelist.linkdefinition.UIValueFunction.Function;
import org.cotrix.web.manage.client.codelist.common.ItemPanel.ItemEditor;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkDefinitionEditor implements ItemEditor<UILinkDefinition> {
	
	private LinkDefinitionDetailsPanel detailsPanel;
	private UILinkDefinition definition;
	private boolean editing = false;
	
	public LinkDefinitionEditor(UILinkDefinition linkDefinition, LinkDefinitionsCodelistInfoProvider codelistInfoProvider) {
		this.definition = linkDefinition;
		detailsPanel = new LinkDefinitionDetailsPanel(codelistInfoProvider);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Void> handler) {
		return detailsPanel.addValueChangeHandler(handler);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		detailsPanel.fireEvent(event);
	}

	@Override
	public void read() {
		definition.setName(ValueUtils.getValue(detailsPanel.getName()));
		definition.setTargetCodelist(detailsPanel.getCodelist());
		definition.setValueFunction(detailsPanel.getValueFunction());
		definition.setValueType(detailsPanel.getValueType());
		definition.setAttributes(detailsPanel.getAttributes());
	}

	@Override
	public void write() {
		detailsPanel.setName(ValueUtils.getLocalPart(definition.getName()));
		detailsPanel.setCodelist(definition.getTargetCodelist(), definition.getValueType());
		detailsPanel.setValueFunction(definition.getValueFunction());
		detailsPanel.setAttributes(definition.getAttributes());
	}

	@Override
	public String getLabel() {
		if (!editing) return ValueUtils.getLocalPart(definition.getName());
		
		String name = detailsPanel.getName();
		return name.isEmpty()?"...":name;
	}

	@Override
	public boolean validate() {
		Log.trace("validate LinkDefinition");
		
		boolean valid = true;

		String name = detailsPanel.getName();
		boolean nameValid = name!=null && !name.isEmpty();
		detailsPanel.setValidName(nameValid);
		valid &= nameValid;

		UICodelist codelist = detailsPanel.getCodelist();
		boolean codelistValid = codelist!=null;
		detailsPanel.setValidCodelist(codelistValid);
		valid &= codelistValid;

		UIValueFunction valueFunction = detailsPanel.getValueFunction();
		boolean validFunction = validateValueFunction(valueFunction);
		detailsPanel.setValidFunction(validFunction);
		valid &= validFunction;
		
		valid &= detailsPanel.areAttributesValid();

		return valid;
	}
	
	private boolean validateValueFunction(UIValueFunction valueFunction) {
		Function function = valueFunction.getFunction();
		List<String> arguments = valueFunction.getArguments();

		if (function.getArguments().length != arguments.size()) return false;

		for (String argument:arguments) if (argument.isEmpty()) return false;
		return true;
	}


	@Override
	public UILinkDefinition getItem() {
		return definition;
	}

	@Override
	public IsWidget getView() {
		return detailsPanel;
	}

	@Override
	public boolean isSwitchVisible() {
		return false;
	}

	@Override
	public void startEditing() {
		detailsPanel.setReadOnly(false);
		detailsPanel.setCodelistReadonly(definition.getTargetCodelist() != null); 
		detailsPanel.focusName();
		editing = true;
	}

	@Override
	public void stopEditing() {
		detailsPanel.setReadOnly(true);
		editing = false;
		
	}
	

	@Override
	public String getLabelValue() {
		return "";
	}

}
