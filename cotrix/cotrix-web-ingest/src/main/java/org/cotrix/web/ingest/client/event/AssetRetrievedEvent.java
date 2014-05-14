package org.cotrix.web.ingest.client.event;

import org.cotrix.web.ingest.shared.AssetInfo;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AssetRetrievedEvent extends GenericEvent {
	
	private AssetInfo asset;

	public AssetRetrievedEvent(AssetInfo asset) {
		this.asset = asset;
	}

	public AssetInfo getAsset() {
		return asset;
	}
}
