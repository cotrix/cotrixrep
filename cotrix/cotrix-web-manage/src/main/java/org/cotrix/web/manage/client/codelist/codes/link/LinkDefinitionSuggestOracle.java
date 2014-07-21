/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.link;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cotrix.web.common.client.util.CachedSuggestOracle;
import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.manage.shared.UILinkDefinitionInfo;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkDefinitionSuggestOracle extends CachedSuggestOracle<LinkDefinitionSuggestOracle.LinkDefinitionSuggestion> {

	public void loadCache(List<UILinkDefinitionInfo> definitions) {
		setCache(toSuggestions(definitions));
	}
	
	private Set<LinkDefinitionSuggestion> toSuggestions(List<UILinkDefinitionInfo> definitions) {
		if (definitions.isEmpty()) return Collections.emptySet();
		Set<LinkDefinitionSuggestion> suggestions = new HashSet<LinkDefinitionSuggestion>(definitions.size());
		for (UILinkDefinitionInfo type:definitions) suggestions.add(new LinkDefinitionSuggestion(type));
		return suggestions;
	}
	
	public static class LinkDefinitionSuggestion implements Suggestion {
		
		public static String toDisplayString(UILinkDefinitionInfo definition) {
			if (definition==null) return "";
			return ValueUtils.getLocalPart(definition.getName());
		}
		
		private UILinkDefinitionInfo linkDefinition;
		private String detailsString;

		public LinkDefinitionSuggestion(UILinkDefinitionInfo linkDefinition) {
			this.linkDefinition = linkDefinition;
			this.detailsString = toDisplayString(linkDefinition);
		}

		public UILinkDefinitionInfo getLinkDefinition() {
			return linkDefinition;
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
					+ ((linkDefinition == null) ? 0 : linkDefinition.hashCode());
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
			LinkDefinitionSuggestion other = (LinkDefinitionSuggestion) obj;
			if (linkDefinition == null) {
				if (other.linkDefinition != null)
					return false;
			} else if (!linkDefinition.equals(other.linkDefinition))
				return false;
			return true;
		}
	}
}
