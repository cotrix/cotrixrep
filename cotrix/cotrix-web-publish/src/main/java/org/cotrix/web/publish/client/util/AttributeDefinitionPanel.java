package org.cotrix.web.publish.client.util;

import org.cotrix.web.share.client.resources.CommonResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeDefinitionPanel extends Composite {

	private static AttributeDefinitionPanelUiBinder uiBinder = GWT.create(AttributeDefinitionPanelUiBinder.class);

	interface AttributeDefinitionPanelUiBinder extends UiBinder<Widget, AttributeDefinitionPanel> {
	}

	@UiField TextBox name;
	@UiField Label attributeLabel;
	@UiField TextBox type;
	@UiField Label inLabel;
	@UiField TextBox language;

	@UiField Style style;

	interface Style extends CssResource {
		String listBoxError();
	}

	public AttributeDefinitionPanel() {
		
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setName(String name) {
		this.name.setText(name);
	}
	
	public void setType(String type) {
		this.type.setText(type);
	}
	
	public void setLanguage(String language) {
		this.language.setText(language);
	}

	protected void setLanguagePanelVisibile(boolean visible)
	{
		inLabel.setVisible(visible);
		language.setVisible(visible);
	}	

	public void setNormalStyle(){
		name.setStyleName(CommonResources.INSTANCE.css().listBox());
	}

	public void setEnabled(boolean enabled)
	{
		name.setEnabled(enabled);
		attributeLabel.setStyleName(CommonResources.INSTANCE.css().paddedTextDisabled(), !enabled);
		type.setEnabled(enabled);
		inLabel.setStyleName(CommonResources.INSTANCE.css().paddedTextDisabled(), !enabled);
		language.setEnabled(enabled);
	}
}
