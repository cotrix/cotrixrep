/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists.user;

import org.cotrix.web.permissionmanager.shared.UIUser;

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
		public void onUserAdded(UIUser user);
	}

	@UiField(provided=true) SuggestBox usernameBox;

	protected UserAddPanelListener listener;

	protected UIUser selectedUser;

	public UserAddPanel() {
		setupUsernameBox();
		initWidget(uiBinder.createAndBindUi(this));
	}

	/**
	 * @param listener the listener to set
	 */
	public void setListener(UserAddPanelListener listener) {
		this.listener = listener;
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
