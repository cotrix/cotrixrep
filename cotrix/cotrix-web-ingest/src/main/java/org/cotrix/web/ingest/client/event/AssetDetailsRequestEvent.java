package org.cotrix.web.ingest.client.event;

import org.cotrix.web.ingest.shared.AssetInfo;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AssetDetailsRequestEvent extends GenericEvent {
	
	private AssetInfo assetInfo;

	public AssetDetailsRequestEvent(AssetInfo assetInfo) {
		this.assetInfo = assetInfo;
	}

	public AssetInfo getAsset() {
		return assetInfo;
	}

}
