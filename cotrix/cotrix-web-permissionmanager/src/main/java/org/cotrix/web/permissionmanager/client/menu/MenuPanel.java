/**
 * 
 */
package org.cotrix.web.permissionmanager.client.menu;

import org.cotrix.web.permissionmanager.client.PermissionBus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MenuPanel extends ResizeComposite {

	interface MenuPanelUiBinder extends UiBinder<Widget, MenuPanel> {
	}
	
	interface MenuResources extends CellTree.Resources {
		
		public static final MenuResources INSTANCE = GWT.create(MenuResources.class);
		
	    /**
	     * The styles used in this widget.
	     */
	    @Source("MenuStyle.css")
	    MenuStyle cellTreeStyle();

		public interface MenuStyle extends CellTree.Style {
			
			String versionItem();
			
		}
	}
	
	@UiField CellTree menuTree;

	@Inject @PermissionBus
	protected EventBus bus;

	@Inject
	protected void init(MenuPanelUiBinder uiBinder) {
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
					bus.fireEvent(new MenuSelectedEvent(menuArea.getAdminArea()));
				}
			}});
		
		MenuResources resources = GWT.create(MenuResources.class);
		MenuTreeViewModel menuTreeViewModel = new MenuTreeViewModel(selectionModel);
		CellTree tree = new CellTree(menuTreeViewModel, null, resources);
		return tree;
	}
	
	
}
