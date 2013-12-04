/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists.tree;

import org.cotrix.web.permissionmanager.client.PermissionServiceAsync;
import org.cotrix.web.permissionmanager.shared.CodelistGroup;
import org.cotrix.web.share.client.error.ManagedFailureCallback;
import org.cotrix.web.share.client.util.FilteredCachedDataProvider;
import org.cotrix.web.share.shared.DataWindow;

import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistGroupsDataProvider extends FilteredCachedDataProvider<CodelistGroup> {
	
	@Inject
	protected PermissionServiceAsync service;

	@Override
	protected void onRangeChanged(final HasData<CodelistGroup> display) {

		loadData();
	}
	
	public void loadData() {
		service.getCodelistGroups(new ManagedFailureCallback<DataWindow<CodelistGroup>>() {

			@Override
			public void onSuccess(DataWindow<CodelistGroup> result) {
				updateData(result);
			}
		});
	}

}
