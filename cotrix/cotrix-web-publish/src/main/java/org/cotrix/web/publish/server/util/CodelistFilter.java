/**
 * 
 */
package org.cotrix.web.publish.server.util;

import org.cotrix.web.common.server.util.Ranges.Predicate;
import org.cotrix.web.common.server.util.ValueUtils;
import org.cotrix.web.common.shared.codelist.UICodelist;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistFilter implements Predicate<UICodelist> {
	
	private String term;

	public CodelistFilter(String term) {
		this.term = term.toLowerCase();
	}

	@Override
	public boolean apply(UICodelist codelist) {
		if (codelist == null) return false;
		
		if (ValueUtils.getLocalPart(codelist.getName()).toLowerCase().contains(term)) return true;
		if (codelist.getVersion().toLowerCase().contains(term)) return true;
		if (codelist.getState()!=null &&  codelist.getState().toString().toLowerCase().contains(term)) return true;
		
		return false;
	}


}
