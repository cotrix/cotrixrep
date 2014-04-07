/**
 * 
 */
package org.cotrix.web.manage.client.codelist.link;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.link.UILinkType;
import org.cotrix.web.manage.client.codelist.link.LinkTypeHeader.Button;
import org.cotrix.web.manage.client.codelist.link.LinkTypeHeader.HeaderListener;

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
public class LinkTypePanel extends Composite implements HasEditing {

	public interface LinkTypePanelListener {
		public void onSave(UILinkType details);
		public void onCancel();
		public void onSelect();
	}

	private boolean editable;
	private boolean editing;

	private LinkTypeHeader header;
	private LinkTypeDetailsPanel detailsPanel;
	private LinkTypePanelListener listener;
	private UILinkType currentLinkType;

	private LinkDisclosurePanel disclosurePanel;

	private String id = Document.get().createUniqueId();

	public LinkTypePanel(CodelistInfoProvider codelistInfoProvider) {
		header = new LinkTypeHeader();
		disclosurePanel = new LinkDisclosurePanel(header);
		disclosurePanel.setWidth("100%");
		disclosurePanel.setAnimationEnabled(true);

		detailsPanel = new LinkTypeDetailsPanel(codelistInfoProvider);
		disclosurePanel.add(detailsPanel);
		initWidget(disclosurePanel);

		detailsPanel.addValueChangeHandler(new ValueChangeHandler<UILinkType>() {

			@Override
			public void onValueChange(ValueChangeEvent<UILinkType> event) {
				System.out.println(event.getValue());
				UILinkType linkTypeDetail = event.getValue();

				validate(linkTypeDetail);
			}
		});

		disclosurePanel.addCloseHandler(new CloseHandler<LinkDisclosurePanel>() {

			@Override
			public void onClose(CloseEvent<LinkDisclosurePanel> event) {
				header.setEditVisible(false);
				header.setControlsVisible(false);
				fireSelected();
			}
		});

		disclosurePanel.addOpenHandler(new OpenHandler<LinkDisclosurePanel>() {

			@Override
			public void onOpen(OpenEvent<LinkDisclosurePanel> event) {
				updateHeaderButtons();
				fireSelected();
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

	public void setLinkType(UILinkType linkType) {
		detailsPanel.setLinkType(linkType);
		this.currentLinkType = linkType;
		header.setHeaderLabel(ValueUtils.getLocalPart(linkType.getName()));
		validate(linkType);
	}

	public void setListener(LinkTypePanelListener listener) {
		this.listener = listener;
	}

	private void onSave() {
		stopEdit();
		UILinkType newType = detailsPanel.getLinkType();
		if (currentLinkType!=null) newType.setId(currentLinkType.getId());
		currentLinkType = detailsPanel.getLinkType();
		if (listener!=null) listener.onSave(currentLinkType);
		header.setHeaderLabel(ValueUtils.getLocalPart(currentLinkType.getName()));
	}

	private void onEdit() {
		startEdit();
		validate(detailsPanel.getLinkType());
	}

	public void enterEditMode() {
		editing = true;
		disclosurePanel.setOpen(true);
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
		if (currentLinkType != null && !currentLinkType.equals(detailsPanel.getLinkType())) detailsPanel.setLinkType(currentLinkType);
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

	private void validate(UILinkType detail) {
		boolean valid = true;

		String name = ValueUtils.getLocalPart(detail.getName());
		boolean nameValid = name!=null && !name.isEmpty();
		detailsPanel.setValidName(nameValid);
		valid &= nameValid;

		UICodelist codelist = detail.getTargetCodelist();
		boolean codelistValid = codelist!=null;
		detailsPanel.setValidCodelist(codelistValid);
		valid &= codelistValid;

		//we use object reference
	/*	if (detail.getFunction() == LinkTypeDetailsPanel.OTHER_FUNCTION) {
			String function = detail.getCustomFunction();
			boolean validFunction = function!=null && !function.isEmpty();
			detailsPanel.setValidFunction(validFunction);
			valid &= validFunction;
		}*/

		System.out.println("Valid? "+valid);
		header.setSaveVisible(valid);

	}

	@Override
	public void setEditable(boolean editable) {
		this.editable = editable;
		updateHeaderButtons();
	}

}
