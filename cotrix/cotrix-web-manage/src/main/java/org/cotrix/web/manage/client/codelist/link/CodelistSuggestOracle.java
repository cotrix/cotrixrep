/**
 * 
 */
package org.cotrix.web.manage.client.codelist.link;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.shared.codelist.UICodelist;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistSuggestOracle extends CachedSuggestOracle<CodelistSuggestOracle.CodelistSuggestion> {

	public void loadCache(List<UICodelist> codelists) {
		setCache(toSuggestions(codelists));
	}
	
	private Set<CodelistSuggestion> toSuggestions(List<UICodelist> codelists) {
		if (codelists.isEmpty()) return Collections.emptySet();
		Set<CodelistSuggestion> suggestions = new HashSet<CodelistSuggestion>(codelists.size());
		for (UICodelist codelist:codelists) {
			//if (codelist.getId().equals(codelistId)) continue;
			suggestions.add(new CodelistSuggestion(codelist));
		}
		return suggestions;
	}
	
	public static class CodelistSuggestion implements Suggestion {
		
		public static String toDisplayString(UICodelist codelist) {
			if (codelist==null) return "";
			return ValueUtils.getLocalPart(codelist.getName()) + " ("+codelist.getVersion()+")";
		}
		
		private UICodelist codelist;
		private String detailsString;

		/**
		 * @param name
		 */
		public CodelistSuggestion(UICodelist codelist) {
			this.codelist = codelist;
			this.detailsString = toDisplayString(codelist);
		}

		public UICodelist getCodelist() {
			return codelist;
		}

		@Override
		public String getDisplayString() {
			return detailsString;
		}

		@Override
		public String getReplacementString() {
			return detailsString;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((codelist == null) ? 0 : codelist.hashCode());
			return result;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CodelistSuggestion other = (CodelistSuggestion) obj;
			if (codelist == null) {
				if (other.codelist != null)
					return false;
			} else if (!codelist.equals(other.codelist))
				return false;
			return true;
		}
	}
}
