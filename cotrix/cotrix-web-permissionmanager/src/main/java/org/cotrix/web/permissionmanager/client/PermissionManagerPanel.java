/**
 * 
 */
package org.cotrix.web.permissionmanager.client;

import org.cotrix.web.permissionmanager.client.application.ApplicationPermissionPanel;
import org.cotrix.web.permissionmanager.client.codelists.CodelistsPermissionsPanel;
import org.cotrix.web.permissionmanager.client.menu.MenuPanel;
import org.cotrix.web.permissionmanager.client.menu.MenuSelectedEvent;
import org.cotrix.web.permissionmanager.client.preferences.PreferencesPanel;
import org.cotrix.web.permissionmanager.client.profile.ProfilePanel;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PermissionManagerPanel extends ResizeComposite {

	interface PermissionManagerPanelUiBinder extends UiBinder<Widget, PermissionManagerPanel> {}
	interface PermissionManagerPanelEventBinder extends EventBinder<PermissionManagerPanel> {}

	@Inject @UiField(provided=true) MenuPanel menuPanel;
	
	@UiField DeckLayoutPanel contentPanel;
	@Inject @UiField(provided=true) ApplicationPermissionPanel applicationPermissionPanel;
	@Inject @UiField(provided=true) CodelistsPermissionsPanel codelistsPermissionspanel;
	@Inject @UiField(provided=true) PreferencesPanel preferencesPanel;
	@Inject @UiField(provided=true) ProfilePanel profilePanel;
	
	@Inject
	protected void init(PermissionManagerPanelUiBinder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		switchContent(AdminArea.PROFILE);
	}
	
	@Inject
	protected void bind(@PermissionBus EventBus bus, PermissionManagerPanelEventBinder eventBinder) {
		eventBinder.bindEventHandlers(this, bus);
	}
	
	@EventHandler
	protected void onMenuSelected(MenuSelectedEvent event) {
		switchContent(event.getAdminArea());
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
