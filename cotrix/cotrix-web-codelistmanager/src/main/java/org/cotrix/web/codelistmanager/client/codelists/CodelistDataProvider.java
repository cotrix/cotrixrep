/**
 * 
 */
package org.cotrix.web.codelistmanager.client.codelists;

import java.util.List;

import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.shared.CodelistGroup;
import org.cotrix.web.share.shared.DataWindow;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistDataProvider extends AsyncDataProvider<CodelistGroup> {
	
	@Inject
	protected ManagerServiceAsync managerService;

	@Override
	protected void onRangeChanged(HasData<CodelistGroup> display) {
		loadData();
	}
	
	public void loadData()
	{
		managerService.getCodelistsGrouped(new AsyncCallback<DataWindow<CodelistGroup>>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Codelist retrieving failed", caught);
			}

			@Override
			public void onSuccess(DataWindow<CodelistGroup> result) {
				List<CodelistGroup> assets = result.getData();
				Log.trace("loaded "+assets.size()+" codelists");
				updateRowCount(result.getTotalSize(), true);
				updateRowData(0, assets);
				
			}
		});		
	}

}
