/**
 * 
 */
package org.cotrix.web.users.client.codelists;

import org.cotrix.web.users.client.PermissionServiceAsync;
import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.util.FilteredCachedDataProvider;
import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.users.shared.UIUserDetails;

import com.google.gwt.view.client.Range;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UsersDetailsDataProvider extends FilteredCachedDataProvider<UIUserDetails> {

	@Inject
	protected PermissionServiceAsync service;


	@Override
	protected void onRangeChanged(final Range range) {
		service.getUsersDetails(new ManagedFailureCallback<DataWindow<UIUserDetails>>() {

			@Override
			public void onSuccess(DataWindow<UIUserDetails> result) {
				updateData(result, range);
			}
		});
	}

}
