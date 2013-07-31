/**
 * 
 */
package org.cotrix.web.importwizard.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AssetsBatch implements IsSerializable {
	
	protected List<AssetInfo> assets;
	protected int totalSize;
	
	public AssetsBatch(){}
	
	/**
	 * @param assets
	 * @param totalSize
	 */
	public AssetsBatch(List<AssetInfo> assets, int totalSize) {
		this.assets = assets;
		this.totalSize = totalSize;
	}

	/**
	 * @return the assets
	 */
	public List<AssetInfo> getAssets() {
		return assets;
	}

	/**
	 * @param assets the assets to set
	 */
	public void setAssets(List<AssetInfo> assets) {
		this.assets = assets;
	}

	/**
	 * @return the totaleSize
	 */
	public int getTotalSize() {
		return totalSize;
	}

	/**
	 * @param totalSize the totalSize to set
	 */
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
	
	

}
