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
import org.cotrix.web.manage.shared.UILinkTypeInfo;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkTypeSuggestOracle extends CachedSuggestOracle<LinkTypeSuggestOracle.LinkTypeSuggestion> {

	public void loadCache(List<UILinkTypeInfo> types) {
		setCache(toSuggestions(types));
	}
	
	private Set<LinkTypeSuggestion> toSuggestions(List<UILinkTypeInfo> types) {
		if (types.isEmpty()) return Collections.emptySet();
		Set<LinkTypeSuggestion> suggestions = new HashSet<LinkTypeSuggestion>(types.size());
		for (UILinkTypeInfo type:types) suggestions.add(new LinkTypeSuggestion(type));
		return suggestions;
	}
	
	public static class LinkTypeSuggestion implements Suggestion {
		
		public static String toDisplayString(UILinkTypeInfo type) {
			if (type==null) return "";
			return ValueUtils.getLocalPart(type.getName());
		}
		
		private UILinkTypeInfo linkType;
		private String detailsString;

		/**
		 * @param name
		 */
		public LinkTypeSuggestion(UILinkTypeInfo linkType) {
			this.linkType = linkType;
			this.detailsString = toDisplayString(linkType);
		}

		public UILinkTypeInfo getLinkType() {
			return linkType;
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
					+ ((linkType == null) ? 0 : linkType.hashCode());
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
			LinkTypeSuggestion other = (LinkTypeSuggestion) obj;
			if (linkType == null) {
				if (other.linkType != null)
					return false;
			} else if (!linkType.equals(other.linkType))
				return false;
			return true;
		}
	}
}
