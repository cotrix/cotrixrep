/**
 * 
 */
package org.cotrix.web.manage.client.codelists;

import java.util.List;

import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.util.FilteredCachedDataProvider;
import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.manage.client.ManagerServiceAsync;
import org.cotrix.web.manage.shared.CodelistGroup;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CodelistsDataProvider extends FilteredCachedDataProvider<CodelistGroup> {
	
	@Inject
	protected ManagerServiceAsync managerService;

	@Override
	protected void onRangeChanged(HasData<CodelistGroup> display) {
		final Range range = display.getVisibleRange();
		onRangeChanged(range);
	}
	
	public void loadData()
	{
		onRangeChanged((Range)null);
	}

	@Override
	protected void onRangeChanged(final Range range) {
		managerService.getCodelistsGrouped(new ManagedFailureCallback<DataWindow<CodelistGroup>>() {

			@Override
			public void onSuccess(DataWindow<CodelistGroup> result) {
				List<CodelistGroup> groups = result.getData();
				Log.trace("loaded "+groups.size()+" codelists");
				if (range == null) updateData(groups, new Range(0, result.getTotalSize()), result.getTotalSize());
				else updateData(groups, range, result.getTotalSize());
					
			}
		});		
	}
	

}
