/**
 * 
 */
package org.cotrix.web.manage.client.codelist.link;

import java.util.List;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.client.widgets.CustomDisclosurePanel;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.manage.client.util.LabelHeader;
import org.cotrix.web.manage.client.util.LabelHeader.Button;
import org.cotrix.web.manage.client.util.LabelHeader.HeaderListener;
import org.cotrix.web.manage.shared.UICodeInfo;
import org.cotrix.web.manage.shared.UILinkTypeInfo;

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
public class LinkPanel extends Composite implements HasEditing {

	public interface LinkPanelListener {
		public void onSave(UILink link);
		public void onCancel();
		public void onSelect();
	}

	private boolean editable;
	private boolean editing;

	private LabelHeader header;
	private LinkDetailsPanel detailsPanel;
	private LinkPanelListener listener;
	private UILink currentLink;

	private CustomDisclosurePanel disclosurePanel;

	private String id = Document.get().createUniqueId();

	public LinkPanel(LinksCodelistInfoProvider codelistInfoProvider) {
		header = new LabelHeader();
		disclosurePanel = new CustomDisclosurePanel(header);
		disclosurePanel.setWidth("100%");
		disclosurePanel.setAnimationEnabled(true);

		detailsPanel = new LinkDetailsPanel(codelistInfoProvider);
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
		});

		detailsPanel.setReadOnly(true);
		editing = false;
		editable = false;
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

	public void setLink(UILink link) {
		setupLinkPanel(link);
		this.currentLink = link;
		header.setHeaderLabel(ValueUtils.getLocalPart(link.getTypeName()));
		validate();
	}

	public void setListener(LinkPanelListener listener) {
		this.listener = listener;
	}

	private void onSave() {
		stopEdit();
		currentLink = getLink();
		if (listener!=null) listener.onSave(currentLink);
		header.setHeaderLabel(ValueUtils.getLocalPart(currentLink.getTypeName()));
	}

	private void onEdit() {
		startEdit();
		validate();
	}

	private UILink getLink() {
		String id = currentLink!=null?currentLink.getId():null;
		UILinkTypeInfo type = detailsPanel.getLinkType();
		UICodeInfo code = detailsPanel.getCode();
		List<UIAttribute> attributes = detailsPanel.getAttributes();
		
		return new UILink(id, type.getId(), type.getName(), code.getId(), code.getName(), null, attributes);
	}

	public void enterEditMode() {
		editable = true;
		editing = true;
		disclosurePanel.setOpen(true);
		detailsPanel.setValueVisible(false);
		startEdit();
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
		if (currentLink != null && !currentLink.equals(getLink())) setupLinkPanel(currentLink);
	}

	private void setupLinkPanel(UILink link) {
		detailsPanel.setLinkType(link.getId(), link.getTypeName());
		detailsPanel.setCode(link.getTargetId(), link.getTargetName());
		detailsPanel.setValue(link.getValue());
		detailsPanel.setAttributes(link.getAttributes());
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
		boolean valid = true;

		UILinkTypeInfo linkType = detailsPanel.getLinkType();
		boolean linkTypeValid = linkType!=null;
		detailsPanel.setValidLinkType(linkTypeValid);
		valid &= linkTypeValid;
		
		UICodeInfo code = detailsPanel.getCode();
		boolean codeValid = code!=null;
		detailsPanel.setValidLinkType(codeValid);
		valid &= codeValid;

		
		valid &= detailsPanel.areAttributesValid();

		Log.trace("Valid ? "+valid);
		header.setSaveVisible(valid);

	}

	@Override
	public void setEditable(boolean editable) {
		this.editable = editable;
		updateHeaderButtons();
	}

}
