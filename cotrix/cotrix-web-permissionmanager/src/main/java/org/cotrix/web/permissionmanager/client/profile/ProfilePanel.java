/**
 * 
 */
package org.cotrix.web.permissionmanager.client.profile;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class ProfilePanel extends Composite {

	interface ProfilePanelUiBinder extends UiBinder<Widget, ProfilePanel> {
	}

	@Inject
	protected void init(ProfilePanelUiBinder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
