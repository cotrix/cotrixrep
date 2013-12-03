/**
 * 
 */
package org.cotrix.web.permissionmanager.client.matrix.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.cotrix.web.permissionmanager.client.PermissionServiceAsync;
import org.cotrix.web.permissionmanager.client.codelists.CodelistRolesRowDataProvider;
import org.cotrix.web.permissionmanager.shared.RolesRow;
import org.cotrix.web.permissionmanager.shared.UIUser;
import org.cotrix.web.share.client.error.ManagedFailureCallback;
import org.cotrix.web.share.client.util.DataUpdatedEvent;
import org.cotrix.web.share.client.util.DataUpdatedEvent.DataUpdatedHandler;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class UserSuggestOracle extends SuggestOracle {
	
	@Inject
	protected PermissionServiceAsync service;
	
	protected List<UIUser> cachedUsers;
	
	protected Set<UIUser> codelistsUsers = new HashSet<UIUser>();
	
	@Inject
	protected void init(final CodelistRolesRowDataProvider rolesRowDataProvider) {
		rolesRowDataProvider.addDataUpdatedHandler(new DataUpdatedHandler() {
			
			@Override
			public void onDataUpdated(DataUpdatedEvent event) {
				codelistsUsers.clear();
				for (RolesRow row:rolesRowDataProvider.getCache()) {
					codelistsUsers.add(row.getUser());
				}
			}
		});
	}

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
		Log.trace("codelistsUsers: "+codelistsUsers);
		Log.trace("users: "+users);
		
		List<Suggestion> suggestions = new ArrayList<SuggestOracle.Suggestion>();
		Iterator<UIUser> usersIterator = users.iterator();
		while(usersIterator.hasNext() && suggestions.size()<request.getLimit()) {
			UIUser user = usersIterator.next();
			if (!codelistsUsers.contains(user) && user.getUsername().toLowerCase().contains(query.toLowerCase())) {
				Suggestion suggestion = new UserSuggestion(user);
				suggestions.add(suggestion);
			}
		}

		Response response = new Response(suggestions);
		
		callback.onSuggestionsReady(request, response);
	}

}
