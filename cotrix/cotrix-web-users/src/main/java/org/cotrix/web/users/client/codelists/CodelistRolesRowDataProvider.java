/**
 * 
 */
package org.cotrix.web.users.client.codelists;

import org.cotrix.web.users.client.UsersServiceAsync;
import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.util.SortedCachedDataProvider;
import org.cotrix.web.common.shared.ColumnSortInfo;
import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.users.shared.RolesRow;

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
	protected UsersServiceAsync service;

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
