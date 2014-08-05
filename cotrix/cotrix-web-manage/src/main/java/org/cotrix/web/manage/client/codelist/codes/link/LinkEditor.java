/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.link;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanel.ItemEditor;
import org.cotrix.web.manage.shared.UICodeInfo;
import org.cotrix.web.manage.shared.UILinkDefinitionInfo;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkEditor implements ItemEditor<UILink> {
	
	private UILink link;
	private LinkDetailsPanel detailsPanel;
	
	public LinkEditor(UILink link, LinksCodelistInfoProvider codelistInfoProvider) {
		this.link = link;
		this.detailsPanel = new LinkDetailsPanel(codelistInfoProvider);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Void> handler) {
		return detailsPanel.addValueChangeHandler(handler);
	}

	@Override
	public void startEditing() {
		detailsPanel.setValueVisible(false);
		detailsPanel.setReadOnly(false);
	}

	@Override
	public void stopEditing() {
		detailsPanel.setReadOnly(true);
	}

	@Override
	public void onEdit(AsyncCallback<Boolean> callBack) {
		callBack.onSuccess(true);
	}
	
	@Override
	public void onSave() {
		detailsPanel.setValueLoaderVisible(true);
	}

	@Override
	public void read() {
		UILinkDefinitionInfo type = detailsPanel.getLinkDefinition();
		link.setDefinitionId(type!=null?type.getId():null);
		link.setDefinitionName(type!=null?type.getName():null);
		
		UICodeInfo code = detailsPanel.getCode();
	
		//if the code is changed the value need to be recalculated
		if (code == null || code.getId() == null || !code.getId().equals(link.getTargetId())) {
			link.setValue(null);
			detailsPanel.setValueLoaderVisible(true);
		}
		
		link.setTargetId(code!=null?code.getId():null);
		link.setTargetName(code!=null?code.getName():null);
		
	

		link.setAttributes(detailsPanel.getAttributes());
	}

	@Override
	public void write() {
		detailsPanel.setLinkDefinition(link.getDefinitionId(), link.getDefinitionName());
		detailsPanel.setCode(link.getTargetId(), link.getTargetName());
		detailsPanel.setValue(link.getValue());
		detailsPanel.setValueVisible(link.getValue()!=null);
		detailsPanel.setValueLoaderVisible(false);
		detailsPanel.setAttributes(link.getAttributes());
	}

	@Override
	public String getLabel() {
		return ValueUtils.getLocalPart(link.getDefinitionName());
	}

	@Override
	public String getLabelValue() {
		return "";
	}

	@Override
	public boolean validate() {
		detailsPanel.setValue("to be computed...");
		
		boolean valid = true;

		UILinkDefinitionInfo linkType = detailsPanel.getLinkDefinition();
		boolean linkTypeValid = linkType!=null;
		detailsPanel.setValidLinkDefinition(linkTypeValid);
		valid &= linkTypeValid;
		
		UICodeInfo code = detailsPanel.getCode();
		boolean codeValid = code!=null;
		detailsPanel.setValidCode(codeValid);
		valid &= codeValid;

		valid &= detailsPanel.areAttributesValid();

		
		return valid;
	}

	@Override
	public UILink getItem() {
		return link;
	}

	@Override
	public IsWidget getView() {
		return detailsPanel;
	}

	@Override
	public boolean isSwitchVisible() {
		return true;
	}

	@Override
	public ImageResource getBullet() {
		return null;
	}

}
