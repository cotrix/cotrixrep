/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists;

import org.cotrix.web.permissionmanager.client.PermissionService;
import org.cotrix.web.permissionmanager.client.PermissionServiceAsync;
import org.cotrix.web.permissionmanager.shared.RolesRow;
import org.cotrix.web.share.client.error.ManagedFailureCallback;
import org.cotrix.web.share.client.util.CachedDataProvider;
import org.cotrix.web.share.shared.DataWindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.view.client.HasData;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistRolesRowDataProvider extends CachedDataProvider<RolesRow> {
	
	protected PermissionServiceAsync service = GWT.create(PermissionService.class);
	
	protected String codelistId;

	/**
	 * @param codelistId
	 */
	public CodelistRolesRowDataProvider(String codelistId) {
		this.codelistId = codelistId;
	}

	/**
	 * @param codelistId the codelistId to set
	 */
	public void setCodelistId(String codelistId) {
		this.codelistId = codelistId;
	}

	@Override
	protected void onRangeChanged(final HasData<RolesRow> display) {
		
		service.getCodelistRolesRows(codelistId, new ManagedFailureCallback<DataWindow<RolesRow>>() {
			
			@Override
			public void onSuccess(DataWindow<RolesRow> result) {
				updateData(result, display.getVisibleRange());
			}
		});
	}

}
