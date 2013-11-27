/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists.user;

import org.cotrix.web.permissionmanager.shared.UIUser;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class UserAddPanel extends ResizeComposite {

	interface UserAddPanelUiBinder extends UiBinder<Widget, UserAddPanel> {
	}

	public interface UserAddPanelListener {
		public void onUserAdded(UIUser user);
	}

	@UiField(provided=true) SuggestBox usernameBox;

	protected UserAddPanelListener listener;

	protected UIUser selectedUser;

	@Inject
	protected void init(UserAddPanelUiBinder uiBinder, UserSuggestOracle oracle) {
		setupUsernameBox(oracle);
		initWidget(uiBinder.createAndBindUi(this));
	}

	/**
	 * @param listener the listener to set
	 */
	public void setListener(UserAddPanelListener listener) {
		this.listener = listener;
	}

	protected void setupUsernameBox(UserSuggestOracle oracle) {
		usernameBox = new SuggestBox(oracle);
		usernameBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				Log.trace("selected suggestion "+event.getSelectedItem());
				if (event.getSelectedItem() instanceof UserSuggestion) {
					UserSuggestion userSuggestion = (UserSuggestion)event.getSelectedItem();
					listener.onUserAdded(userSuggestion.getUser());
					usernameBox.setText("");
				}
			}
		});

	}

}
