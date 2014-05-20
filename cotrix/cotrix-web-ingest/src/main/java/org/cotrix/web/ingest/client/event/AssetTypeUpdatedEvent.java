package org.cotrix.web.ingest.client.event;

import org.cotrix.web.ingest.shared.UIAssetType;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AssetTypeUpdatedEvent extends GenericEvent {

	private UIAssetType codeListType;
	
	public AssetTypeUpdatedEvent(UIAssetType codeListType) {
		this.codeListType = codeListType;
	}

	/**
	 * @return the codeListType
	 */
	public UIAssetType getAssetType() {
		return codeListType;
	}
}
