/**
 * 
 */
package org.cotrix.web.manage.client.codelist.attribute;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.data.event.DataSavedEvent;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.event.ManagerBus;

import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeNameSuggestOracle extends SuggestOracle {
	
	@Inject @CurrentCodelist
	private String codelistId;
	
	@Inject
	private ManageServiceAsync service;
	
	private Set<AttributeNameSuggestion> suggestionsCache = new HashSet<AttributeNameSuggestion>();
	
	@Inject
	private void init() {
		loadCache();
	}
	
	@Inject
	private void bind(@ManagerBus EventBus eventBus) {
		eventBus.addHandler(DataSavedEvent.TYPE, new DataSavedEvent.DataSavedHandler() {
			
			@Override
			public void onDataSaved(DataSavedEvent event) {
				if (codelistId.equals(event.getCodelistId())) loadCache();
			}
		});
	}

	@Override
	public void requestSuggestions(final Request request, final Callback callback) {
		Response response = new Response(filter(request.getQuery(), request.getLimit()));
		callback.onSuggestionsReady(request, response);
	}
	
	private Set<Suggestion> filter(String query, int limit) {
		Set<Suggestion> suggestions = new HashSet<Suggestion>(limit);
		for (AttributeNameSuggestion nameSuggestion:suggestionsCache) {
			if (nameSuggestion.getName().getLocalPart().toLowerCase().contains(query)) suggestions.add(nameSuggestion);
			if (suggestions.size()>=limit) break;
		}
		return suggestions;
	}
	
	public void addSuggestion(UIQName name) {
		suggestionsCache.add(new AttributeNameSuggestion(name));
	}
	
	private void loadCache() {
		service.getAttributeNames(codelistId, new ManagedFailureCallback<Set<UIQName>>() {
			
			@Override
			public void onSuccess(Set<UIQName> result) {
				suggestionsCache = toSuggestions(result);
			}

		});
	}
	
	private Set<AttributeNameSuggestion> toSuggestions(Set<UIQName> names) {
		if (names.isEmpty()) return Collections.emptySet();
		Set<AttributeNameSuggestion> suggestions = new HashSet<AttributeNameSuggestion>(names.size());
		for (UIQName name:names) suggestions.add(new AttributeNameSuggestion(name));
		return suggestions;
	}
	
	public class AttributeNameSuggestion implements Suggestion {
		
		private UIQName name;

		/**
		 * @param name
		 */
		public AttributeNameSuggestion(UIQName name) {
			this.name = name;
		}

		@Override
		public String getDisplayString() {
			return name.getLocalPart();
		}

		@Override
		public String getReplacementString() {
			return name.getLocalPart();
		}

		public UIQName getName() {
			return name;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((name == null) ? 0 : name.hashCode());
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
			AttributeNameSuggestion other = (AttributeNameSuggestion) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		private AttributeNameSuggestOracle getOuterType() {
			return AttributeNameSuggestOracle.this;
		}
		
	}
}
