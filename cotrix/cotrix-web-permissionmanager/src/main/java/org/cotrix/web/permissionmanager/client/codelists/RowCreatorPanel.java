/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.cotrix.web.permissionmanager.client.codelists.matrix.User;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RowCreatorPanel extends ResizeComposite {

	private static RowCreatorPanelUiBinder uiBinder = GWT
			.create(RowCreatorPanelUiBinder.class);

	interface RowCreatorPanelUiBinder extends UiBinder<Widget, RowCreatorPanel> {
	}
	
	protected static List<User> USERS = Arrays.asList(
			new User("1", "Federico De Faveri"),
			new User("2", "Fabio Simeoni"),
			new User("3", "Anton Ellenbroek"),
			new User("4", "Aureliano Gentile"),
			new User("5", "Erik Van Ingen")	
			);
	
	@UiField SuggestBox usernameBox;
	
	protected User selectedUser;

	public RowCreatorPanel() {

		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiFactory
	protected SuggestBox setupUsernameBox() {
		SuggestOracle oracle = new SuggestOracle() {

			@Override
			public void requestSuggestions(Request request, Callback callback) {

				String query = request.getQuery();
				Log.trace("requestSuggestions query "+query);
				
				List<Suggestion> suggestions = new ArrayList<SuggestOracle.Suggestion>();
				Iterator<User> usersIterator = USERS.iterator();
				while(usersIterator.hasNext() && suggestions.size()<request.getLimit()) {
					User user = usersIterator.next();
					if (user.getUsername().toLowerCase().contains(query.toLowerCase())) {
						Suggestion suggestion = new UserSuggestion(user);
						suggestions.add(suggestion);
					}
				}

				Response response = new Response(suggestions);
				
				callback.onSuggestionsReady(request, response);
			}
			
		};
		SuggestBox usernameBox = new SuggestBox(oracle);
		
		return usernameBox;
	}
	
	protected class UserSuggestion implements Suggestion {
		protected User user;

		/**
		 * @param user
		 */
		public UserSuggestion(User user) {
			this.user = user;
		}

		@Override
		public String getDisplayString() {
			return user.getUsername();
		}

		@Override
		public String getReplacementString() {
			return user.getUsername();
		}
		
	}

}
