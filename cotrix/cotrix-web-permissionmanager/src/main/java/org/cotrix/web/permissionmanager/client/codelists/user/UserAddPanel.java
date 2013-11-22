/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists.user;

import org.cotrix.web.permissionmanager.shared.User;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserAddPanel extends ResizeComposite {

	private static UserAddPanelUiBinder uiBinder = GWT
			.create(UserAddPanelUiBinder.class);

	interface UserAddPanelUiBinder extends UiBinder<Widget, UserAddPanel> {
	}

	public interface UserAddPanelListener {
		public void onUserAdded(User user);
	}


	@UiField(provided=true) SuggestBox usernameBox;

	protected UserAddPanelListener listener;

	protected User selectedUser;

	public UserAddPanel(UserAddPanelListener listener) {
		this.listener = listener;
		setupUsernameBox();
		initWidget(uiBinder.createAndBindUi(this));
	}

	protected void setupUsernameBox() {
		UserSuggestOracle oracle = new UserSuggestOracle();
		usernameBox = new SuggestBox(oracle);
		usernameBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				Log.trace("selected suggestion "+event.getSelectedItem());
				if (event.getSelectedItem() instanceof UserSuggestion) {
					UserSuggestion userSuggestion = (UserSuggestion)event.getSelectedItem();
					listener.onUserAdded(userSuggestion.getUser());
				}
			}
		});

	}

}
