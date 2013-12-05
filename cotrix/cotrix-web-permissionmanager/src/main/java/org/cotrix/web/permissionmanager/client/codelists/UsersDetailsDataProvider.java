/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists;

import org.cotrix.web.permissionmanager.client.PermissionServiceAsync;
import org.cotrix.web.permissionmanager.shared.UIUserDetails;
import org.cotrix.web.share.client.error.ManagedFailureCallback;
import org.cotrix.web.share.client.util.FilteredCachedDataProvider;
import org.cotrix.web.share.shared.DataWindow;

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
