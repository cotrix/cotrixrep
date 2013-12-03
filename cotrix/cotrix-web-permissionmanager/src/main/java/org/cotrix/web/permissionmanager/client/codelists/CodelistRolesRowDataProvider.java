/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists;

import org.cotrix.web.permissionmanager.client.PermissionServiceAsync;
import org.cotrix.web.permissionmanager.client.matrix.EditorRow;
import org.cotrix.web.permissionmanager.shared.RolesRow;
import org.cotrix.web.share.client.error.ManagedFailureCallback;
import org.cotrix.web.share.client.util.CachedDataProvider;
import org.cotrix.web.share.shared.DataWindow;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CodelistRolesRowDataProvider extends CachedDataProvider<RolesRow> {

	@Inject
	protected PermissionServiceAsync service;

	protected String codelistId;

	/**
	 * @param codelistId the codelistId to set
	 */
	public void setCodelistId(String codelistId) {
		this.codelistId = codelistId;
	}

	@Override
	protected void onRangeChanged(final HasData<RolesRow> display) {
		if (codelistId!=null) {
			service.getCodelistRolesRows(codelistId, new ManagedFailureCallback<DataWindow<RolesRow>>() {

				@Override
				public void onSuccess(DataWindow<RolesRow> result) {
					Log.trace("rows: "+result);
					result.getData().add(new EditorRow());
					updateData(result, display.getVisibleRange());
				}
			});
		}
	}

}
