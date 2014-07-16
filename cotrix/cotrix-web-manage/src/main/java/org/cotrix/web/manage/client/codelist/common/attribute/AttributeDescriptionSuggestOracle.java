/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common.attribute;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.cotrix.web.common.client.factory.UIDefaults;
import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.attributetype.UIAttributeType;
import org.cotrix.web.manage.client.codelist.cache.AttributeTypesCache;
import org.cotrix.web.manage.client.di.CurrentCodelist;

import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeDescriptionSuggestOracle extends SuggestOracle {
	
	@Inject
	private UIDefaults defaults;
	
	private Set<Suggestion> defaultSuggestions;
	
	@Inject @CurrentCodelist
	private AttributeTypesCache attributeTypesCache;
	
	private boolean onlyDefaults;

	public void setOnlyDefaults(boolean onlyDefaults) {
		this.onlyDefaults = onlyDefaults;
	}

	@Override
	public void requestSuggestions(final Request request, final Callback callback) {
		
		if (onlyDefaults) callback.onSuggestionsReady(request, new Response(filter(getDefaultSuggestions(), request.getQuery(), request.getLimit())));
		else {
			Set<Suggestion> suggestions = new TreeSet<Suggestion>(getDefaultSuggestions());
			for (UIAttributeType type:attributeTypesCache.getItems()) suggestions.add(new AttributeDescriptionSuggestion(type.getType()));
			
			Set<Suggestion> filteredSuggestions =filter(suggestions, request.getQuery(), request.getLimit());
			callback.onSuggestionsReady(request, new Response(filteredSuggestions));
		}
	}
	
	private Set<Suggestion> filter(Set<Suggestion> suggestions, String query, int limit) {
		Set<Suggestion> filteredSuggestions = new HashSet<Suggestion>(limit);
		String lowerCaseQuery = query.toLowerCase();
		for (Suggestion suggestion:suggestions) {
			if (suggestion.getDisplayString().toLowerCase().contains(lowerCaseQuery)) filteredSuggestions.add(suggestion);
			if (filteredSuggestions.size()>=limit) break;
		}
		return filteredSuggestions;
	}
	
	private Set<Suggestion> getDefaultSuggestions() {
		if (defaultSuggestions == null) {
			defaultSuggestions = new TreeSet<Suggestion>();
			for (UIQName description:defaults.defaultTypes()) defaultSuggestions.add(new AttributeDescriptionSuggestion(description));
		}
		return defaultSuggestions;
	}

	public class AttributeDescriptionSuggestion implements Suggestion, Comparable<AttributeDescriptionSuggestion> {
		
		private UIQName description;

		public AttributeDescriptionSuggestion(UIQName description) {
			this.description = description;
		}

		@Override
		public String getDisplayString() {
			return description.getLocalPart();
		}

		@Override
		public String getReplacementString() {
			return description.getLocalPart();
		}

		public UIQName getDescription() {
			return description;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((description == null) ? 0 : description.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AttributeDescriptionSuggestion other = (AttributeDescriptionSuggestion) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (description == null) {
				if (other.description != null)
					return false;
			} else if (!description.equals(other.description))
				return false;
			return true;
		}

		private AttributeDescriptionSuggestOracle getOuterType() {
			return AttributeDescriptionSuggestOracle.this;
		}

		@Override
		public int compareTo(AttributeDescriptionSuggestion o) {
			return String.CASE_INSENSITIVE_ORDER.compare(ValueUtils.getLocalPart(description), ValueUtils.getLocalPart(o.description));
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("AttributeDescriptionSuggestion [getDisplayString()=");
			builder.append(getDisplayString());
			builder.append("]");
			return builder.toString();
		}
	}
}
