/**
 * 
 */
package org.cotrix.web.codelistmanager.client.codelists;

import org.cotrix.web.codelistmanager.shared.CodeListGroup;
import org.cotrix.web.codelistmanager.shared.CodeListGroup.Version;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.TreeViewModel;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListTreeModel implements TreeViewModel {
	
	protected static final AbstractCell<CodeListGroup> GROUP_CELL = new AbstractCell<CodeListGroup>() {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				CodeListGroup value, SafeHtmlBuilder sb) {
			sb.appendEscaped(value.getName());
		}
	};
	
	protected static final AbstractCell<Version> VERSION_CELL = new AbstractCell<Version>() {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				Version value, SafeHtmlBuilder sb) {
			sb.appendEscaped(value.getVersion());
		}
	};
	
	protected CodeListDataProvider dataProvider;
	protected SelectionModel<Version> selectionModel;

	/**
	 * @param dataProvider
	 */
	public CodeListTreeModel(CodeListDataProvider dataProvider, SelectionModel<Version> selectionModel) {
		this.dataProvider = dataProvider;
		this.selectionModel = selectionModel;
	}

	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		if (value == null) {
			return new DefaultNodeInfo<CodeListGroup>(dataProvider, GROUP_CELL);
		}
		
		if (value instanceof CodeListGroup) {
			CodeListGroup group = (CodeListGroup) value;
			return new DefaultNodeInfo<Version>(new ListDataProvider<Version>(group.getVersions()), VERSION_CELL, selectionModel, null);
		}
		return null;
	}

	@Override
	public boolean isLeaf(Object value) {
		return value instanceof Version;
	}

}
