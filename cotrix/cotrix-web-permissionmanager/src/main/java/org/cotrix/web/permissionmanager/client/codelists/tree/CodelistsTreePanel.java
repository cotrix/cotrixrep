/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists.tree;

import org.cotrix.web.permissionmanager.client.codelists.tree.CodelistGroup.Version;
import org.cotrix.web.permissionmanager.client.resources.CodelistsResources;
import org.cotrix.web.share.client.util.SingleSelectionModel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistsTreePanel extends ResizeComposite {

	private static CodelistsTreePanelUiBinder uiBinder = GWT
			.create(CodelistsTreePanelUiBinder.class);

	interface CodelistsTreePanelUiBinder extends
	UiBinder<Widget, CodelistsTreePanel> {
	}

	@UiField CellTree codelistsTree;

	public CodelistsTreePanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiFactory
	protected CellTree setupCodelistsTree() {

		final SingleSelectionModel<Version> selectionModel = new SingleSelectionModel<Version>();
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				System.out.println("SELECTED "+event);
				Version selected = selectionModel.getSelectedObject();
				if (selected != null) {
					//presenter.onCodelistItemSelected(selected.toUICodelist());
				}
			}
		});

		CellTree codelistsTree = new CellTree(new CodelistTreeModel(selectionModel), null, CodelistsResources.INSTANCE);

		//codelists.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		codelistsTree.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		return codelistsTree;
	}

}
