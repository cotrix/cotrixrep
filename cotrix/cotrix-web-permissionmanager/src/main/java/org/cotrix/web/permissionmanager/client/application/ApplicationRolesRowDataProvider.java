/**
 * 
 */
package org.cotrix.web.permissionmanager.client.application;

import org.cotrix.web.permissionmanager.client.PermissionServiceAsync;
import org.cotrix.web.permissionmanager.shared.RolesRow;
import org.cotrix.web.share.client.error.ManagedFailureCallback;
import org.cotrix.web.share.client.util.SortedCachedDataProvider;
import org.cotrix.web.share.shared.ColumnSortInfo;
import org.cotrix.web.share.shared.DataWindow;

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
	protected PermissionServiceAsync service;
	
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
