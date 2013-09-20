/**
 * 
 */
package org.cotrix.web.codelistmanager.client.view;

import java.util.List;

import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.UICodelist;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListDataProvider extends AsyncDataProvider<UICodelist> {
	
	@Inject
	protected ManagerServiceAsync managerService;

	@Override
	protected void onRangeChanged(HasData<UICodelist> display) {
		
		final Range range = display.getVisibleRange();
		
		managerService.getCodelists(range, new AsyncCallback<DataWindow<UICodelist>>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Codelist retrieving failed", caught);
			}

			@Override
			public void onSuccess(DataWindow<UICodelist> result) {
				List<UICodelist> assets = result.getData();
				Log.trace("loaded "+assets.size()+" codelists");
				updateRowCount(result.getTotalSize(), true);
				updateRowData(range.getStart(), assets);
				
			}
		});
				
	}

}
