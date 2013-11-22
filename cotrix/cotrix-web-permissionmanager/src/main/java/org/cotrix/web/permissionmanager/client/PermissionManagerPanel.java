/**
 * 
 */
package org.cotrix.web.permissionmanager.client;

import org.cotrix.web.permissionmanager.client.application.ApplicationPermissionPanel;
import org.cotrix.web.permissionmanager.client.codelists.CodelistsPermissionsPanel;
import org.cotrix.web.permissionmanager.client.menu.MenuPanel;
import org.cotrix.web.permissionmanager.client.menu.MenuPanel.MenuListener;
import org.cotrix.web.permissionmanager.client.preferences.PreferencesPanel;
import org.cotrix.web.permissionmanager.client.profile.ProfilePanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PermissionManagerPanel extends ResizeComposite {

	private static PermissionManagerPanelUiBinder uiBinder = GWT
			.create(PermissionManagerPanelUiBinder.class);

	interface PermissionManagerPanelUiBinder extends
			UiBinder<Widget, PermissionManagerPanel> {
	}

	@UiField MenuPanel menuPanel;
	
	@UiField DeckLayoutPanel contentPanel;
	@UiField ApplicationPermissionPanel applicationPermissionPanel;
	@UiField CodelistsPermissionsPanel codelistsPermissionspanel;
	@UiField PreferencesPanel preferencesPanel;
	@UiField ProfilePanel profilePanel;
	
	public PermissionManagerPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		
		switchContent(AdminArea.PROFILE);
	}
	
	@UiFactory
	protected MenuPanel setupMenuPanel() {
		MenuListener menuListener = new MenuListener() {
			
			@Override
			public void onMenuSelected(AdminArea area) {
				switchContent(area);
			}
		};
		
		return new MenuPanel(menuListener);
	}
	
	protected void switchContent(AdminArea area) {
		switch (area) {
			case APPLICATION_PERMISSIONS: contentPanel.showWidget(applicationPermissionPanel); break;
			case CODELISTS_PERMISSIONS: contentPanel.showWidget(codelistsPermissionspanel); break;
			case PREFERENCES: contentPanel.showWidget(preferencesPanel); break;
			case PROFILE: contentPanel.showWidget(profilePanel); break;
		}
	}
	

}
