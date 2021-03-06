/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.linkdefinition;

import java.util.List;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIRange;
import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition;
import org.cotrix.web.common.shared.codelist.linkdefinition.UIValueFunction;
import org.cotrix.web.common.shared.codelist.linkdefinition.UIValueFunction.Function;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanel.ItemEditor;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkDefinitionEditor implements ItemEditor<UILinkDefinition> {
	
	private LinkDefinitionDetailsPanel detailsPanel;
	private UILinkDefinition definition;
	private boolean editing = false;
	
	public LinkDefinitionEditor(UILinkDefinition linkDefinition, LinkDefinitionDetailsPanel detailsPanel) {
		this.definition = linkDefinition;
		this.detailsPanel = detailsPanel;
	}

	@Override
	public void read() {
		definition.setName(detailsPanel.getName());
		definition.setTargetCodelist(detailsPanel.getCodelist());
		definition.setValueFunction(detailsPanel.getValueFunction());
		definition.setRange(detailsPanel.getRange());
		definition.setValueType(detailsPanel.getValueType());
		definition.setAttributes(detailsPanel.getAttributes());
	}

	@Override
	public void write() {
		detailsPanel.setName(definition.getName());
		detailsPanel.setCodelist(definition.getTargetCodelist(), definition.getValueType());
		detailsPanel.setRange(definition.getRange());
		detailsPanel.setValueFunction(definition.getValueFunction());
		detailsPanel.setAttributes(definition.getAttributes());
	}

	@Override
	public String getHeaderTitle() {
		if (!editing) return ValueUtils.getLocalPart(definition.getName());
		
		UIQName name = detailsPanel.getName();
		return name.isEmpty()?"...":name.getLocalPart();
	}

	@Override
	public boolean validate() {
		Log.trace("validate LinkDefinition");
		
		boolean valid = true;

		UIQName name = detailsPanel.getName();
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
		
		UIRange range = detailsPanel.getRange();
		boolean rangeValid = range.getMin()!=Integer.MIN_VALUE && range.getMax()!=Integer.MIN_VALUE;
		detailsPanel.setRangeFieldValid(rangeValid);
		valid &= rangeValid;
		
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
	public void onStartEditing() {
		detailsPanel.setReadOnly(false);
		detailsPanel.setCodelistReadonly(definition.getTargetCodelist() != null); 
		detailsPanel.focusName();
		editing = true;
	}

	@Override
	public void onStopEditing() {
		detailsPanel.setReadOnly(true);
		editing = false;
		
	}
	
	@Override
	public String getHeaderSubtitle() {
		return "";
	}

	@Override
	public void onEdit(AsyncCallback<Boolean> callBack) {
		callBack.onSuccess(true);
	}

	@Override
	public void onSave() {
	}
}
