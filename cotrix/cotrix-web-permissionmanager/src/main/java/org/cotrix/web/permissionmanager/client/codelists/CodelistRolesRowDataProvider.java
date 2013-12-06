/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists;

import org.cotrix.web.permissionmanager.client.PermissionServiceAsync;
import org.cotrix.web.permissionmanager.shared.RolesRow;
import org.cotrix.web.share.client.error.ManagedFailureCallback;
import org.cotrix.web.share.client.util.SortedCachedDataProvider;
import org.cotrix.web.share.shared.ColumnSortInfo;
import org.cotrix.web.share.shared.DataWindow;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CodelistRolesRowDataProvider extends SortedCachedDataProvider<RolesRow> {

	@Inject
	protected PermissionServiceAsync service;

	protected String codelistId;
	public CodelistRolesRowDataProvider() {
		super(RolesRow.USER_NAME_FIELD);
	}

	/**
	 * @param codelistId the codelistId to set
	 */
	public void setCodelistId(String codelistId) {
		this.codelistId = codelistId;
	}

	@Override
	protected void onRangeChange(final Range range, ColumnSortInfo sortInfo) {
		if (codelistId!=null) {
			service.getCodelistRolesRows(codelistId, range, sortInfo, new ManagedFailureCallback<DataWindow<RolesRow>>() {

				@Override
				public void onSuccess(DataWindow<RolesRow> result) {
					Log.trace("rows: "+result);
					updateData(result, range);
				}
			});
		}
	}

}
