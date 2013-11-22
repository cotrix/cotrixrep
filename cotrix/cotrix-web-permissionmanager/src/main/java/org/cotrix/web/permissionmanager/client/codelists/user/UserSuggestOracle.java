/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cotrix.web.permissionmanager.client.PermissionService;
import org.cotrix.web.permissionmanager.client.PermissionServiceAsync;
import org.cotrix.web.permissionmanager.shared.UIUser;
import org.cotrix.web.share.client.error.ManagedFailureCallback;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserSuggestOracle extends SuggestOracle {
	
	protected PermissionServiceAsync service = GWT.create(PermissionService.class);
	
	protected List<UIUser> cachedUsers;

	@Override
	public void requestSuggestions(Request request, Callback callback) {
		if (cachedUsers == null) loadCacheAndSugges(request, callback);
		else doSuggestion(request, callback, cachedUsers);
	}
	
	protected void loadCacheAndSugges(final Request request, final Callback callback) {
		service.getUsers(new ManagedFailureCallback<List<UIUser>>() {

			@Override
			public void onSuccess(List<UIUser> result) {
				cachedUsers = result;
				doSuggestion(request, callback, result);				
			}
		});
	}
	
	protected void doSuggestion(Request request, Callback callback, List<UIUser> users) {
		String query = request.getQuery();
		Log.trace("requestSuggestions query "+query);
		
		List<Suggestion> suggestions = new ArrayList<SuggestOracle.Suggestion>();
		Iterator<UIUser> usersIterator = users.iterator();
		while(usersIterator.hasNext() && suggestions.size()<request.getLimit()) {
			UIUser user = usersIterator.next();
			if (user.getUsername().toLowerCase().contains(query.toLowerCase())) {
				Suggestion suggestion = new UserSuggestion(user);
				suggestions.add(suggestion);
			}
		}

		Response response = new Response(suggestions);
		
		callback.onSuggestionsReady(request, response);
	}

}
