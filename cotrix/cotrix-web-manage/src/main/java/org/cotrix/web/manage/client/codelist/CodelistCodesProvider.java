/**
 * 
 */
package org.cotrix.web.manage.client.codelist;

import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.util.CachedDataProviderExperimental;
import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.codelist.CodelistEditor.GroupColumn;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.shared.CodelistEditorSortInfo;
import org.cotrix.web.manage.shared.Group;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistCodesProvider extends CachedDataProviderExperimental<UICode> {

	@Inject
	protected ManageServiceAsync managerService;

	@Inject @CurrentCodelist
	protected String codelistId;

	@Override
	protected void onRangeChanged(final Range range, final ManagedFailureCallback<DataWindow<UICode>> callback) {
		CodelistEditorSortInfo sortInfo = getSortInfo();
		managerService.getCodelistCodes(codelistId, range, sortInfo, new ManagedFailureCallback<DataWindow<UICode>>() {

			@Override
			public void onSuccess(DataWindow<UICode> result) {
				Log.trace("loaded "+result.getData().size()+" rows");
				callback.onSuccess(result);
				//updateData(result.getData(), range, result.getTotalSize());
			}
		});
	}
	
	protected CodelistEditorSortInfo getSortInfo() {
		for (HasData<UICode> display:getDataDisplays()) {
			if (display instanceof AbstractCellTable) {
				AbstractCellTable<UICode> abstractCellTable = (AbstractCellTable<UICode>) display;
				ColumnSortList columnSortList = abstractCellTable.getColumnSortList();
				if (columnSortList.size()>0) {
					ColumnSortInfo columnSortInfo = columnSortList.get(0);
					
					Column<?, ?> column = columnSortInfo.getColumn();
					if (column instanceof CodelistEditor.CodeColumn) return new CodelistEditorSortInfo.CodeNameSortInfo(columnSortInfo.isAscending());
					if (column instanceof CodelistEditor.GroupColumn) {
						GroupColumn groupColumn = (CodelistEditor.GroupColumn)column;
						Group group = groupColumn.getGroup();
						return group.getSortInfo(columnSortInfo.isAscending());
					}
				}
			}
		}
		return null;
	}
}
