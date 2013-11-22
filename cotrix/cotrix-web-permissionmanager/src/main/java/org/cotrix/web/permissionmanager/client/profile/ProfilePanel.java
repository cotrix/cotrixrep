/**
 * 
 */
package org.cotrix.web.permissionmanager.client.profile;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ProfilePanel extends Composite {

	private static ProfilePanelUiBinder uiBinder = GWT
			.create(ProfilePanelUiBinder.class);

	interface ProfilePanelUiBinder extends UiBinder<Widget, ProfilePanel> {
	}

	
	public ProfilePanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
