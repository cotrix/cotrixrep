package org.cotrix.web.ingest.client.event;

import org.cotrix.web.ingest.shared.AssetInfo;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AssetSelectedEvent extends GenericEvent {
	
	private AssetInfo selectedAsset;

	public AssetSelectedEvent(AssetInfo selectedAsset) {
		this.selectedAsset = selectedAsset;
	}

	public AssetInfo getSelectedAsset() {
		return selectedAsset;
	}
}
