/**
 * 
 */
package org.cotrix.web.importwizard.client.step.selection;

import java.util.List;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.shared.AssetInfo;
import org.cotrix.web.importwizard.shared.AssetsBatch;

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
		importService.getAssets(range, new AsyncCallback<AssetsBatch>() {
			
			@Override
			public void onSuccess(AssetsBatch batch) {
				List<AssetInfo> assets = batch.getAssets();
				Log.trace("loaded "+assets.size()+" assets");
				for (AssetInfo assetInfo:assets) Log.trace("Asset: "+assetInfo);
				updateRowCount(batch.getTotalSize(), true);
				updateRowData(range.getStart(), assets);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				//TODO show the error to the user?
				Log.error("An error occurred loading the assets", caught);
			}
		});
	}

}
