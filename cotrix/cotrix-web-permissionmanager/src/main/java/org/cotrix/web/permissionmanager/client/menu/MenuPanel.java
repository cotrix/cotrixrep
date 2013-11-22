/**
 * 
 */
package org.cotrix.web.permissionmanager.client.menu;

import org.cotrix.web.permissionmanager.client.AdminArea;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MenuPanel extends ResizeComposite {

	private static MenuPanelUiBinder uiBinder = GWT
			.create(MenuPanelUiBinder.class);

	interface MenuPanelUiBinder extends UiBinder<Widget, MenuPanel> {
	}
	
	public interface MenuListener {
		public void onMenuSelected(AdminArea area);
	}
	
	@UiField CellTree menuTree;
	
	protected MenuListener listener;

	public MenuPanel(MenuListener listener) {
		this.listener = listener;
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiFactory
	protected CellTree setupTree() {
		
		final SingleSelectionModel<MenuItem> selectionModel = new SingleSelectionModel<MenuItem>();
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler(){

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				MenuItem menuItem = selectionModel.getSelectedObject();
				if (menuItem instanceof MenuArea) {
					MenuArea menuArea = (MenuArea)menuItem;
					listener.onMenuSelected(menuArea.getAdminArea());
				}
			}});
		
		MenuTreeViewModel menuTreeViewModel = new MenuTreeViewModel(selectionModel);
		CellTree tree = new CellTree(menuTreeViewModel, null);
		return tree;
	}
	
	
}
