/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common.attribute;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.cotrix.web.common.client.factory.UIDefaults;
import org.cotrix.web.common.client.util.CachedSuggestOracle;
import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.attributetype.UIAttributeType;
import org.cotrix.web.manage.client.codelist.cache.AttributeTypesCache;
import org.cotrix.web.manage.client.di.CurrentCodelist;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeDescriptionSuggestOracle extends CachedSuggestOracle<AttributeDescriptionSuggestOracle.AttributeDescriptionSuggestion> {
	
	@Inject
	private UIDefaults defaults;
	
	@Inject @CurrentCodelist
	private AttributeTypesCache attributeTypesCache;
	
	public void setupOnlyDefaults() {
		Set<AttributeDescriptionSuggestion> suggestions = new TreeSet<AttributeDescriptionSuggestion>();
		for (UIQName description:defaults.defaultTypes()) suggestions.add(new AttributeDescriptionSuggestion(description));
		setCache(suggestions);
	}
	
	
	public void setupWithAttributeTypes(final AsyncCallback<Void> callback) {
		
		final Set<AttributeDescriptionSuggestion> suggestions = new TreeSet<AttributeDescriptionSuggestion>();
		for (UIQName description:defaults.defaultTypes()) suggestions.add(new AttributeDescriptionSuggestion(description));
		attributeTypesCache.getItems(new AsyncCallback<Collection<UIAttributeType>>() {
			
			@Override
			public void onSuccess(Collection<UIAttributeType> result) {
				for (UIAttributeType type:result) suggestions.add(new AttributeDescriptionSuggestion(type.getType()));
				setCache(suggestions);
				callback.onSuccess(null);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
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
	}
}
