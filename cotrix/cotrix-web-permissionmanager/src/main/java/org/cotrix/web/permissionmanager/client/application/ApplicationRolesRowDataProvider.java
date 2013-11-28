/**
 * 
 */
package org.cotrix.web.permissionmanager.client.application;

import org.cotrix.web.permissionmanager.client.PermissionServiceAsync;
import org.cotrix.web.permissionmanager.shared.RolesRow;
import org.cotrix.web.share.client.error.ManagedFailureCallback;
import org.cotrix.web.share.client.util.CachedDataProvider;
import org.cotrix.web.share.shared.DataWindow;

import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class ApplicationRolesRowDataProvider extends CachedDataProvider<RolesRow> {
	
	@Inject
	protected PermissionServiceAsync service;

	@Override
	protected void onRangeChanged(final HasData<RolesRow> display) {
		
		service.getApplicationRolesRows(new ManagedFailureCallback<DataWindow<RolesRow>>() {
			
			@Override
			public void onSuccess(DataWindow<RolesRow> result) {
				updateData(result, display.getVisibleRange());
			}
		});
	}

}
