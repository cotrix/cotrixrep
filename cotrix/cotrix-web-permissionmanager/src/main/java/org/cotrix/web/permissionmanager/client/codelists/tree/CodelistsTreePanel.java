/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists.tree;

import org.cotrix.web.permissionmanager.client.PermissionBus;
import org.cotrix.web.permissionmanager.client.resources.CodelistsResources;
import org.cotrix.web.permissionmanager.shared.CodelistGroup;
import org.cotrix.web.permissionmanager.shared.CodelistGroup.CodelistVersion;
import org.cotrix.web.share.client.resources.CommonResources;
import org.cotrix.web.share.client.util.SingleSelectionModel;
import org.cotrix.web.share.client.util.FilteredCachedDataProvider.Filter;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CodelistsTreePanel extends ResizeComposite {

	interface CodelistsTreePanelUiBinder extends UiBinder<Widget, CodelistsTreePanel> {}

	@UiField TextBox filterTextBox;

	@UiField(provided=true) CellTree codelistsTree;

	@Inject
	protected CodelistGroupsDataProvider dataProvider;

	protected SingleSelectionModel<CodelistVersion> selectionModel;

	@Inject @PermissionBus
	protected EventBus bus;

	@Inject
	protected void init(CodelistsTreePanelUiBinder uiBinder) {
		setupCodelistsTree();
		initWidget(uiBinder.createAndBindUi(this));
		updateSearchBoxStyle();
	}

	@UiHandler("filterTextBox")
	protected void onKeyUp(KeyUpEvent event) {
		Log.trace("onKeyUp value: "+filterTextBox.getValue()+" text: "+filterTextBox.getText());
		updateFilter();
		updateSearchBoxStyle();
	}

	@SuppressWarnings("unchecked")
	protected void updateFilter()
	{
		String filter = filterTextBox.getValue();
		if (filter.isEmpty()) dataProvider.unapplyFilters();
		else {
			dataProvider.applyFilters(new ByNameFilter(filter));
		}
	}

	public void updateSearchBoxStyle() {
		filterTextBox.setStyleName(CommonResources.INSTANCE.css().searchBackground(), filterTextBox.getValue().isEmpty());
	}

	public void refresh() {
		selectionModel.clear();
		dataProvider.loadData();
	}

	protected void setupCodelistsTree() {

		selectionModel = new SingleSelectionModel<CodelistVersion>();
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {

				CodelistVersion selected = selectionModel.getSelectedObject();
				Log.trace("selected version "+selected);

				if (selected != null) {
					bus.fireEvent(new CodelistSelectedEvent(selected));
				}
			}
		});

		codelistsTree = new CellTree(new CodelistTreeModel(selectionModel, dataProvider), null, CodelistsResources.INSTANCE);

		//codelists.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		codelistsTree.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

	}

	protected class ByNameFilter implements Filter<CodelistGroup> {

		protected String name;

		/**
		 * @param name
		 */
		public ByNameFilter(String name) {
			this.name = name.toUpperCase();
		}

		@Override
		public boolean accept(CodelistGroup data) {
			return data.getName().toUpperCase().contains(name);
		}

	}

}
