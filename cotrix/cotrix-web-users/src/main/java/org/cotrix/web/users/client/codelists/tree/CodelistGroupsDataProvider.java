/**
 * 
 */
package org.cotrix.web.users.client.codelists.tree;

import org.cotrix.web.users.client.PermissionServiceAsync;
import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.util.FilteredCachedDataProvider;
import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.users.shared.CodelistGroup;

import com.google.gwt.view.client.Range;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistGroupsDataProvider extends FilteredCachedDataProvider<CodelistGroup> {
	
	@Inject
	protected PermissionServiceAsync service;
	
	public void loadData() {
		service.getCodelistGroups(new ManagedFailureCallback<DataWindow<CodelistGroup>>() {

			@Override
			public void onSuccess(DataWindow<CodelistGroup> result) {
				updateData(result);
			}
		});
	}

	@Override
	protected void onRangeChanged(Range range) {
		loadData();
	}

}
