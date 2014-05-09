/**
 * 
 */
package org.cotrix.web.ingest.server.util;

import org.cotrix.web.common.server.util.Ranges.Predicate;
import org.cotrix.web.ingest.shared.AssetInfo;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AssetInfoFilter implements Predicate<AssetInfo> {
	
	private String term;

	public AssetInfoFilter(String term) {
		this.term = term.toLowerCase();
	}

	@Override
	public boolean apply(AssetInfo asset) {
		if (asset == null) return false;
		
		if (asset.getName().toLowerCase().contains(term)) return true;
		if (asset.getRepositoryName().getLocalPart().toLowerCase().contains(term)) return true;
		if (asset.getType().toLowerCase().contains(term)) return true;
		
		return false;
	}


}
