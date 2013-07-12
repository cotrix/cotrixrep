/**
 * 
 */
package org.cotrix.web.importwizard.client.step.channel;

import org.cotrix.web.importwizard.shared.AssetInfo;

import com.google.gwt.view.client.ProvidesKey;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AssetInfoKeyProvider implements ProvidesKey<AssetInfo> {
	
	public static final AssetInfoKeyProvider INSTANCE = new AssetInfoKeyProvider();

	@Override
	public Object getKey(AssetInfo item) {
		return item==null?null:item.getId();
	}

}
