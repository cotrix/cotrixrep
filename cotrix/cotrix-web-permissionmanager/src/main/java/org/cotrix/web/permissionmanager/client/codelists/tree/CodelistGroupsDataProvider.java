/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists.tree;

import org.cotrix.web.permissionmanager.client.PermissionService;
import org.cotrix.web.permissionmanager.client.PermissionServiceAsync;
import org.cotrix.web.permissionmanager.shared.CodelistGroup;
import org.cotrix.web.share.client.error.ManagedFailureCallback;
import org.cotrix.web.share.client.util.CachedDataProvider;
import org.cotrix.web.share.shared.DataWindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.view.client.HasData;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistGroupsDataProvider extends CachedDataProvider<CodelistGroup> {
	
	protected PermissionServiceAsync service = GWT.create(PermissionService.class);

	@Override
	protected void onRangeChanged(final HasData<CodelistGroup> display) {
		service.getCodelistGroups(new ManagedFailureCallback<DataWindow<CodelistGroup>>() {

			@Override
			public void onSuccess(DataWindow<CodelistGroup> result) {
				updateData(result, display.getVisibleRange());
			}
		});
		
	}

}
