/**
 * 
 */
package org.cotrix.web.importwizard.client.step.selection;

import java.util.ArrayList;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.shared.AssetInfo;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AssetInfoDataProvider extends AsyncDataProvider<AssetInfo> {
	
	@Inject
	protected ImportServiceAsync importService;
	
	/**
	 * @param importService
	 */
	public AssetInfoDataProvider() {
		super(AssetInfoKeyProvider.INSTANCE);
	}

	@Override
	protected void onRangeChanged(HasData<AssetInfo> display) {
		final Range range = display.getVisibleRange();
		Log.trace("onRangeChanged range: "+range);
		importService.getAssets(range, new AsyncCallback<ArrayList<AssetInfo>>() {
			
			@Override
			public void onSuccess(ArrayList<AssetInfo> values) {
				Log.trace("loaded "+values.size()+" assets");
				for (AssetInfo assetInfo:values) Log.trace("Asset: "+assetInfo);
				updateRowCount(values.size(), true);
				updateRowData(range.getStart(), values);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				//TODO show the error to the user?
				Log.error("An error occurred loading the assets", caught);
			}
		});
	}

}
