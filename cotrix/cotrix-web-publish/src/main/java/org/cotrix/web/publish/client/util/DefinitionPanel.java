package org.cotrix.web.publish.client.util;

import org.cotrix.web.publish.client.resources.Resources;
import org.cotrix.web.publish.shared.UIDefinition;
import org.cotrix.web.publish.shared.UIDefinition.DefinitionType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DefinitionPanel extends Composite {

	private static DefinitionPanelUiBinder uiBinder = GWT.create(DefinitionPanelUiBinder.class);

	interface DefinitionPanelUiBinder extends UiBinder<Widget, DefinitionPanel> { }

	interface Style extends CssResource {
		String listBoxError();
		String titleDisabled();
		String subtitleDisabled();
	}
	
	@UiField Image icon;
	@UiField Label title;
	@UiField Label subtitle;

	@UiField Style style;
	
	private ImageResource iconResource;
	private ImageResource iconResourceDisabled;


	public DefinitionPanel() {
		
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setDefinition(UIDefinition definition) {
		
		setIcon(definition.getDefinitionType());
		icon.setResource(iconResource);
		title.setText(definition.getTitle());
		subtitle.setText(definition.getSubTitle());
	}
	
	private void setIcon(DefinitionType type) {
		switch (type) {
			case ATTRIBUTE_DEFINITION: {
				iconResource = Resources.INSTANCE.attributeIcon();
				iconResourceDisabled = Resources.INSTANCE.attributeIconDisabled();
			} break;
			case LINK_DEFINITION: {
				iconResource = Resources.INSTANCE.linkIcon();
				iconResourceDisabled = Resources.INSTANCE.linkIconDisabled();
			} break;
		}
	}
	

	public void setEnabled(boolean enabled)
	{
		icon.setResource(enabled?iconResource:iconResourceDisabled);
		title.setStyleName(style.titleDisabled(), !enabled);
		subtitle.setStyleName(style.subtitleDisabled(), !enabled);
		
	}
}
