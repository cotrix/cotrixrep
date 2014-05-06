/**
 * 
 */
package org.cotrix.web.manage.client.codelist.linktype;

import java.util.List;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.client.widgets.CustomDisclosurePanel;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.common.shared.codelist.linktype.UIValueFunction;
import org.cotrix.web.common.shared.codelist.linktype.UIValueFunction.Function;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemEditingPanel;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemEditingPanelListener;
import org.cotrix.web.manage.client.util.LabelHeader;
import org.cotrix.web.manage.client.util.LabelHeader.Button;
import org.cotrix.web.manage.client.util.LabelHeader.HeaderListener;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Composite;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkTypePanel extends Composite implements ItemEditingPanel<UILinkType> {

	private boolean editable;
	private boolean editing;

	private LabelHeader header;
	private LinkTypeDetailsPanel detailsPanel;
	private ItemEditingPanelListener<UILinkType> listener;
	private UILinkType type;

	private CustomDisclosurePanel disclosurePanel;

	private String id = Document.get().createUniqueId();

	public LinkTypePanel(UILinkType type, LinkTypesCodelistInfoProvider codelistInfoProvider) {
		this.type = type;
		
		header = new LabelHeader();
		disclosurePanel = new CustomDisclosurePanel(header);
		disclosurePanel.setWidth("100%");
		disclosurePanel.setAnimationEnabled(true);

		detailsPanel = new LinkTypeDetailsPanel(codelistInfoProvider);
		disclosurePanel.add(detailsPanel);
		initWidget(disclosurePanel);

		detailsPanel.addValueChangeHandler(new ValueChangeHandler<Void>() {

			@Override
			public void onValueChange(ValueChangeEvent<Void> event) {
				validate();
			}
		});

		disclosurePanel.addCloseHandler(new CloseHandler<CustomDisclosurePanel>() {

			@Override
			public void onClose(CloseEvent<CustomDisclosurePanel> event) {
				header.setEditVisible(false);
				header.setControlsVisible(false);
				fireSelected();
			}
		});

		disclosurePanel.addOpenHandler(new OpenHandler<CustomDisclosurePanel>() {

			@Override
			public void onOpen(OpenEvent<CustomDisclosurePanel> event) {
				updateHeaderButtons();
				fireSelected();
				if (editing) validate();
			}
		});

		header.setListener(new HeaderListener() {

			@Override
			public void onButtonClicked(Button button) {
				switch (button) {
					case EDIT: onEdit(); break;
					case REVERT: onCancel(); break;
					case SAVE: onSave(); break;
				}
			}

			@Override
			public void onSwitchChange(boolean isDown) {
				//explicitly ignored
			}
		});

		detailsPanel.setReadOnly(true);
		editing = false;
		editable = false;
		
		writeType();
		updateHeaderLabel();
	}

	public String getId() {
		return id;
	}

	private void fireSelected() {
		if (listener!=null) listener.onSelect();
	}

	public void setSelected(boolean selected) {
		header.setHeaderSelected(selected);
	}

	private void onSave() {
		stopEdit();
		readType();
		if (listener!=null) listener.onSave(type);
		updateHeaderLabel();
	}

	private void onEdit() {
		startEdit();
		detailsPanel.setCodelistReadonly(true);
		validate();
	}
	
	public void syncWithModel() {
		writeType();
	}

	private void readType() {
		type.setName(ValueUtils.getValue(detailsPanel.getName()));
		type.setTargetCodelist(detailsPanel.getCodelist());
		type.setValueFunction(detailsPanel.getValueFunction());
		type.setValueType(detailsPanel.getValueType());
		type.setAttributes(detailsPanel.getAttributes());
	}

	public void enterEditMode() {
		editable = true;
		editing = true;
		disclosurePanel.setOpen(true);
		startEdit();
		detailsPanel.setCodelistReadonly(false);
	}

	private void startEdit() {
		editing = true;
		detailsPanel.setReadOnly(false);
		updateHeaderButtons();
	}

	private void stopEdit() {
		editing = false;
		detailsPanel.setReadOnly(true);
		updateHeaderButtons();	
	}

	private void onCancel() {
		stopEdit();
		if (listener!=null) listener.onCancel();
		writeType();
	}

	private void writeType() {
		detailsPanel.setName(ValueUtils.getLocalPart(type.getName()));
		detailsPanel.setCodelist(type.getTargetCodelist(), type.getValueType());
		detailsPanel.setValueFunction(type.getValueFunction());
		detailsPanel.setAttributes(type.getAttributes());
	}
	
	private void updateHeaderLabel() {
		header.setHeaderLabel(ValueUtils.getLocalPart(type.getName()));
	}

	private void updateHeaderButtons() {
		if (disclosurePanel.isOpen()) {
			header.setEditVisible(!editing && editable);
			header.setControlsVisible(editing);
			header.setRevertVisible(editing);
			header.setSaveVisible(false);
		} else {
			header.setEditVisible(false);
			header.setControlsVisible(false);
			header.setRevertVisible(false);
			header.setSaveVisible(false);
		}
	}

	private void validate() {
		Log.trace("validate LinkType");
		
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

		Log.trace("Valid ? "+valid);
		header.setSaveVisible(valid);

	}

	private boolean validateValueFunction(UIValueFunction valueFunction) {
		Function function = valueFunction.getFunction();
		List<String> arguments = valueFunction.getArguments();

		if (function.getArguments().length != arguments.size()) return false;

		for (String argument:arguments) if (argument.isEmpty()) return false;
		return true;
	}

	@Override
	public void setEditable(boolean editable) {
		this.editable = editable;
		updateHeaderButtons();
	}

	@Override
	public void setListener(ItemEditingPanelListener<UILinkType> listener) {
		this.listener = listener;
	}

	@Override
	public void setSwitchDown(boolean down) {
		header.setSwitchDown(down);
	}

}
