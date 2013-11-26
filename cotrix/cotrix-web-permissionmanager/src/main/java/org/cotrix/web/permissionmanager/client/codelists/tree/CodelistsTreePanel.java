/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists.tree;

import org.cotrix.web.permissionmanager.client.resources.CodelistsResources;
import org.cotrix.web.permissionmanager.shared.CodelistGroup.CodelistVersion;
import org.cotrix.web.share.client.util.SingleSelectionModel;

import com.allen_sauer.gwt.log.client.Log;
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

	interface CodelistsTreePanelUiBinder extends UiBinder<Widget, CodelistsTreePanel> {
	}
	
	public interface CodelistsTreePanelListener {
		public void onCodelistSelected(CodelistVersion codelist);
	}

	@UiField CellTree codelistsTree;

	protected CodelistsTreePanelListener listener;
	
	public CodelistsTreePanel(CodelistsTreePanelListener listener) {
		this.listener = listener;
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiFactory
	protected CellTree setupCodelistsTree() {

		final SingleSelectionModel<CodelistVersion> selectionModel = new SingleSelectionModel<CodelistVersion>();
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				
				CodelistVersion selected = selectionModel.getSelectedObject();
				Log.trace("selected version "+selected);
				
				if (selected != null) {
					listener.onCodelistSelected(selected);
				}
			}
		});

		CellTree codelistsTree = new CellTree(new CodelistTreeModel(selectionModel), null, CodelistsResources.INSTANCE);

		//codelists.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		codelistsTree.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		return codelistsTree;
	}

}
