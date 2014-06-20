/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common.attribute;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.cotrix.web.common.client.util.CachedSuggestOracle;
import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.shared.codelist.attributetype.UIAttributeType;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeTypeSuggestOracle extends CachedSuggestOracle<AttributeTypeSuggestOracle.AttributeTypeSuggestion> {
	
	private static final Comparator<AttributeTypeSuggestion> COMPARATOR = new Comparator<AttributeTypeSuggestOracle.AttributeTypeSuggestion>() {
		
		@Override
		public int compare(AttributeTypeSuggestion s1, AttributeTypeSuggestion s2) {
			if (s1 == NONE) return -1;
			if (s2 == NONE) return 1;
			return s1.detailsString.compareToIgnoreCase(s2.detailsString);
		}
	};

	public static AttributeTypeSuggestion NONE = new AttributeTypeSuggestion("none");
	
	public void loadCache(Collection<UIAttributeType> types) {
		Set<AttributeTypeSuggestion> suggestions = new TreeSet<AttributeTypeSuggestion>(COMPARATOR);
		suggestions.add(NONE);
		suggestions.addAll(toSuggestions(types));
		setCache(suggestions);
	}
	
	private Set<AttributeTypeSuggestion> toSuggestions(Collection<UIAttributeType> types) {
		if (types.isEmpty()) return Collections.emptySet();
		Set<AttributeTypeSuggestion> suggestions = new HashSet<AttributeTypeSuggestion>(types.size());
		for (UIAttributeType type:types) suggestions.add(new AttributeTypeSuggestion(type));
		return suggestions;
	}
	
	public static class AttributeTypeSuggestion implements Suggestion {
		
		public static String toDisplayString(UIAttributeType type) {
			if (type==null) return "";
			return ValueUtils.getLocalPart(type.getName());
		}
		
		private UIAttributeType attributeType;
		private String detailsString;

		public AttributeTypeSuggestion(UIAttributeType attributeType) {
			this.attributeType = attributeType;
			this.detailsString = toDisplayString(attributeType);
		}
		
		public AttributeTypeSuggestion(String detailsString) {
			this.attributeType = null;
			this.detailsString = detailsString;
		}

		public UIAttributeType getAttributeType() {
			return attributeType;
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
					+ ((attributeType == null) ? 0 : attributeType.hashCode());
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
			AttributeTypeSuggestion other = (AttributeTypeSuggestion) obj;
			if (attributeType == null) {
				if (other.attributeType != null)
					return false;
			} else if (!attributeType.equals(other.attributeType))
				return false;
			return true;
		}
	}
}
