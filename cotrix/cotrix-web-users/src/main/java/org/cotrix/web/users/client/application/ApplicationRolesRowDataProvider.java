/**
 * 
 */
package org.cotrix.web.users.client.application;

import org.cotrix.web.users.client.UsersServiceAsync;
import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.util.SortedCachedDataProvider;
import org.cotrix.web.common.shared.ColumnSortInfo;
import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.users.shared.RolesRow;

import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class ApplicationRolesRowDataProvider extends SortedCachedDataProvider<RolesRow> {
	
	@Inject
	protected UsersServiceAsync service;
	
	public ApplicationRolesRowDataProvider() {
		super(RolesRow.USER_NAME_FIELD);
	}

	@Override
	protected void onRangeChange(final Range range, ColumnSortInfo sortInfo) {
		
		service.getApplicationRolesRows(range, sortInfo, new ManagedFailureCallback<DataWindow<RolesRow>>() {
			
			@Override
			public void onSuccess(DataWindow<RolesRow> result) {
				updateData(result, range);
			}
		});
	}

}
