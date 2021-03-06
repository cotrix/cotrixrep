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
		
		if (ValueUtils.contains(codelist.getName(), term)) return true;
		if (ValueUtils.contains(codelist.getVersion(), term)) return true;
		if (codelist.getState()!=null &&  ValueUtils.contains(codelist.getState().toString(), term)) return true;
		
		return false;
	}


}
