/**
 * 
 */
package org.cotrix.web.manage.client.codelist.link;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CachedSuggestOracle<T extends Suggestion> extends SuggestOracle {
	
	private Set<T> suggestionsCache = new HashSet<T>();
	
	@Override
	public void requestSuggestions(final Request request, final Callback callback) {
		System.out.println("requestSuggestions request: "+request);
		Response response = new Response(filter(request.getQuery(), request.getLimit()));
		callback.onSuggestionsReady(request, response);
	}
	
	private Set<Suggestion> filter(String query, int limit) {
		Set<Suggestion> suggestions = new HashSet<Suggestion>(limit);
		for (T suggestion:suggestionsCache) {
			if (suggestion.getDisplayString().toLowerCase().contains(query)) suggestions.add(suggestion);
			if (suggestions.size()>=limit) break;
		}
		return suggestions;
	}
	
	public void setCache(Set<T> codelists) {
		suggestionsCache = codelists;
	}
}
