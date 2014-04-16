/**
 * 
 */
package org.cotrix.web.manage.client.codelist.link;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cotrix.web.common.client.util.CachedSuggestOracle;
import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.manage.shared.UICodeInfo;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeSuggestOracle extends CachedSuggestOracle<CodeSuggestOracle.CodeSuggestion> {

	public void loadCache(List<UICodeInfo> codes) {
		setCache(toSuggestions(codes));
	}
	
	private Set<CodeSuggestion> toSuggestions(List<UICodeInfo> codes) {
		if (codes.isEmpty()) return Collections.emptySet();
		Set<CodeSuggestion> suggestions = new HashSet<CodeSuggestion>(codes.size());
		for (UICodeInfo code:codes) suggestions.add(new CodeSuggestion(code));
		return suggestions;
	}
	
	public static class CodeSuggestion implements Suggestion {
		
		public static String toDisplayString(UICodeInfo code) {
			if (code==null) return "";
			return ValueUtils.getLocalPart(code.getName());
		}
		
		private UICodeInfo code;
		private String detailsString;

		/**
		 * @param name
		 */
		public CodeSuggestion(UICodeInfo code) {
			this.code = code;
			this.detailsString = toDisplayString(code);
		}

		public UICodeInfo getCode() {
			return code;
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
					+ ((code == null) ? 0 : code.hashCode());
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
			CodeSuggestion other = (CodeSuggestion) obj;
			if (code == null) {
				if (other.code != null)
					return false;
			} else if (!code.equals(other.code))
				return false;
			return true;
		}
	}
}
