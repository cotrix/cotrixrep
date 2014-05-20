/**
 * 
 */
package org.cotrix.web.users.client.codelists.tree;

import org.cotrix.web.common.client.util.FilteredCachedDataProvider.Filter;
import org.cotrix.web.common.client.util.SingleSelectionModel;
import org.cotrix.web.common.client.widgets.SearchBox;
import org.cotrix.web.users.client.UsersBus;
import org.cotrix.web.users.client.resources.CodelistsResources;
import org.cotrix.web.users.shared.CodelistGroup;
import org.cotrix.web.users.shared.CodelistGroup.CodelistVersion;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.CellTree.CellTreeMessages;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.ResizeComposite;
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

	@DefaultLocale("en_US")
	interface TreeMessages extends CellTreeMessages {

		@DefaultMessage("You don''t own any codelist.")
		String emptyTree();
	}

	@UiField SearchBox filterTextBox;

	@UiField(provided=true) CellTree codelistsTree;

	@Inject
	protected CodelistGroupsDataProvider dataProvider;

	protected SingleSelectionModel<CodelistVersion> selectionModel;

	@Inject @UsersBus
	protected EventBus bus;
	
	@Inject
	protected TreeMessages treeMessages;

	@Inject
	protected void init(CodelistsTreePanelUiBinder uiBinder) {
		setupCodelistsTree();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("filterTextBox")
	protected void onValueChange(ValueChangeEvent<String> event) {
		Log.trace("onKeyUp value: "+filterTextBox.getValue());
		updateFilter(event.getValue());
	}

	@SuppressWarnings("unchecked")
	protected void updateFilter(String filter)
	{
		if (filter.isEmpty()) dataProvider.unapplyFilters();
		else {
			dataProvider.applyFilters(new ByNameFilter(filter));
		}
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

		codelistsTree = new CellTree(new CodelistTreeModel(selectionModel, dataProvider), null, CodelistsResources.INSTANCE, treeMessages);

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
