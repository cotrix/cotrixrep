/**
 * 
 */
package org.cotrix.web.permissionmanager.client.application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ApplicationPermissionPanel extends Composite {

	private static ApplicationPermissionPanelUiBinder uiBinder = GWT
			.create(ApplicationPermissionPanelUiBinder.class);

	interface ApplicationPermissionPanelUiBinder extends
			UiBinder<Widget, ApplicationPermissionPanel> {
	}

	public ApplicationPermissionPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
