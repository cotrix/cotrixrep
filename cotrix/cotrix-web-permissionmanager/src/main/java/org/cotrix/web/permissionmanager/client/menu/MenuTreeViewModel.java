/**
 * 
 */
package org.cotrix.web.permissionmanager.client.menu;

import java.util.Arrays;

import org.cotrix.web.permissionmanager.client.AdminArea;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.TreeViewModel;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MenuTreeViewModel implements TreeViewModel {
	
	protected static final MenuItem PROFILE_MENU = new MenuArea("Profile", AdminArea.PROFILE);
	protected static final MenuItem PREFERENCES_MENU = new MenuArea("Preferences", AdminArea.PREFERENCES);

	protected static final MenuItem APPLICATION_MENU = new MenuArea("Application", AdminArea.APPLICATION_PERMISSIONS);
	protected static final MenuItem CODELISTS_MENU = new MenuArea("Codelists", AdminArea.CODELISTS_PERMISSIONS);
	
	protected static final MenuItem PERMISSIONS_MENU = new MenuFolder("Permissions", Arrays.asList(APPLICATION_MENU, CODELISTS_MENU));

	protected static final ListDataProvider<MenuItem> ROOT_PROVIDER = new ListDataProvider<MenuItem>(Arrays.asList(PROFILE_MENU, PREFERENCES_MENU, PERMISSIONS_MENU));
	protected static final ListDataProvider<MenuItem> PERMISSIONS_PROVIDER = new ListDataProvider<MenuItem>(((MenuFolder)PERMISSIONS_MENU).getChildren());
	
	protected static final AbstractCell<MenuItem> MENU_CELL = new AbstractCell<MenuItem>() {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, MenuItem value, SafeHtmlBuilder sb) {
			sb.append(SafeHtmlUtils.fromString(value.getLabel()));
		}
	};
	
	protected DefaultNodeInfo<MenuItem> rootNode;
	protected DefaultNodeInfo<MenuItem> permissionsNode;
	
	public MenuTreeViewModel(SelectionModel<MenuItem> selectionModel) {
		rootNode = new DefaultNodeInfo<MenuItem>(ROOT_PROVIDER, MENU_CELL, selectionModel, null);
		permissionsNode = new DefaultNodeInfo<MenuItem>(PERMISSIONS_PROVIDER, MENU_CELL, selectionModel, null);
	}
	
	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		if (value == null) return rootNode;
		if (value == PERMISSIONS_MENU) return permissionsNode;
		return null;
	}

	@Override
	public boolean isLeaf(Object value) {
		return value instanceof MenuArea;
	}

}
