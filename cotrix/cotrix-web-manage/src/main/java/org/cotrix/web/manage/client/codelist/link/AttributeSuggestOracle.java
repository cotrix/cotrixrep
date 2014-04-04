/**
 * 
 */
package org.cotrix.web.manage.client.codelist.link;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.shared.codelist.link.AttributeType;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeSuggestOracle extends CachedSuggestOracle<AttributeSuggestOracle.AttributeSuggestion> {

	public void loadCache(List<AttributeType> attributes) {
		setCache(toSuggestions(attributes));
	}
	
	private Set<AttributeSuggestion> toSuggestions(List<AttributeType> attributes) {
		if (attributes.isEmpty()) return Collections.emptySet();
		Set<AttributeSuggestion> suggestions = new HashSet<AttributeSuggestion>(attributes.size());
		for (AttributeType attribute:attributes) {
			suggestions.add(new AttributeSuggestion(attribute));
		}
		return suggestions;
	}
	
	public static class AttributeSuggestion implements Suggestion {
		
		public static String toDetailsString(AttributeType attribute) {
			if (attribute == null) return "";
			return ValueUtils.getLocalPart(attribute.getName()) + " " + ValueUtils.getLocalPart(attribute.getType())+" "+attribute.getLanguage();
		}
		
		private AttributeType attribute;
		private String displayString;

		/**
		 * @param name
		 */
		public AttributeSuggestion(AttributeType attribute) {
			this.attribute = attribute;
			displayString = toDetailsString(attribute);
		}

		public AttributeType getAttribute() {
			return attribute;
		}

		@Override
		public String getDisplayString() {
			return displayString;
		}

		@Override
		public String getReplacementString() {
			return displayString;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((attribute == null) ? 0 : attribute.hashCode());
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
			AttributeSuggestion other = (AttributeSuggestion) obj;
			if (attribute == null) {
				if (other.attribute != null)
					return false;
			} else if (!attribute.equals(other.attribute))
				return false;
			return true;
		}
	}
}
