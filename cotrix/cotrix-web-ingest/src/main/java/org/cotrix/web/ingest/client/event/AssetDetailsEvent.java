package org.cotrix.web.ingest.client.event;

import org.cotrix.web.ingest.shared.AssetDetails;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AssetDetailsEvent extends GenericEvent {
	
	private AssetDetails assetDetails;

	public AssetDetailsEvent(AssetDetails assetDetails) {
		this.assetDetails = assetDetails;
	}

	public AssetDetails getAssetDetails() {
		return assetDetails;
	}

}
