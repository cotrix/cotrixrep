/**
 * 
 */
package org.cotrix.web.publish.server.util;

import org.cotrix.web.common.server.util.ValueUtils;
import org.cotrix.web.common.server.util.Ranges.Predicate;
import org.cotrix.web.common.shared.Format;
import org.cotrix.web.publish.shared.UIRepository;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RepositoryFilter implements Predicate<UIRepository> {
	
	private String term;

	public RepositoryFilter(String term) {
		this.term = term.toLowerCase();
	}

	@Override
	public boolean apply(UIRepository asset) {
		if (asset == null) return false;
		
		if (ValueUtils.getLocalPart(asset.getName()).toLowerCase().contains(term)) return true;
		if (asset.getPublishedTypes().toLowerCase().contains(term)) return true;
		for (Format format:asset.getAvailableFormats()) if (format.toString().toLowerCase().contains(term)) return true;
		
		return false;
	}


}
