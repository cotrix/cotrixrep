/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistsPermissionsPanel extends ResizeComposite {

	private static CodelistsPermissionsPanelUiBinder uiBinder = GWT
			.create(CodelistsPermissionsPanelUiBinder.class);

	interface CodelistsPermissionsPanelUiBinder extends
			UiBinder<Widget, CodelistsPermissionsPanel> {
	}

	public CodelistsPermissionsPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		if (visible) onResize();
	}

}
