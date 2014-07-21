/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata;

import org.cotrix.web.common.client.error.ErrorManager;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.client.widgets.LoadingPanel;
import org.cotrix.web.common.client.widgets.dialog.AlertDialog;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.manage.client.codelist.NewStateEvent;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.di.CurrentCodelist;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SplashPanel extends LoadingPanel {

	interface ProfilePanelUiBinder extends UiBinder<Widget, SplashPanel> {}
	interface ProfilePanelEventBinder extends EventBinder<SplashPanel> {}
	
	enum Action {
		LOCK,
		UNLOCK,
		SEAL,
		UNSEAL;
	}
	
	public interface Listener {
		public void onAction(Action action);
	}
	
	@UiField Label name;
	@UiField InlineLabel version;
	@UiField Label state;
	
	@UiField PushButton lock;
	@UiField PushButton unlock;
	@UiField PushButton seal;
	@UiField PushButton unseal;
	
	@Inject
	@UiField(provided=true) AttributeDefinitionsPanel attributeDefinitionsPanel;
	
	@Inject
	@UiField(provided=true) LinkDefinitionsPanel linkDefinitionsPanel;
	
	@Inject
	private AlertDialog alertDialog;
	
	@Inject
	private ErrorManager errorManager;
	
	@Inject @CurrentCodelist
	private UICodelist codelist;
	
	private Listener listener;

	@Inject
	protected void init(ProfilePanelUiBinder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		setAnimated(true);
		name.setText(codelist.getName().getLocalPart());
		name.setTitle(codelist.getName().getLocalPart());
		version.setText(codelist.getVersion());
		state.setText(codelist.getState()!=null?codelist.getState().toString():"");
		
		attributeDefinitionsPanel.loadData();
		linkDefinitionsPanel.loadData();
	}
	
	@EventHandler
	void onNewState(NewStateEvent event) {
		String stateLabel = String.valueOf(event.getState()).toUpperCase();
		state.setText(stateLabel);
	}
	
	@Inject
	protected void bind(@CodelistBus EventBus bus, ProfilePanelEventBinder eventBinder) {
		eventBinder.bindEventHandlers(this, bus);
	}

	public HasEditing getAttributeDefinitionsPanel() {
		return attributeDefinitionsPanel;
	}

	public HasEditing getLinkDefinitionsPanel() {
		return linkDefinitionsPanel;
	}
	
	
	@UiHandler("lock")
	protected void onLockClick(ClickEvent event) {
		listener.onAction(Action.LOCK);
	}
	
	@UiHandler("unlock")
	protected void onUnlockClick(ClickEvent event) {
		listener.onAction(Action.UNLOCK);
	}
	
	@UiHandler("seal")
	protected void onSealClick(ClickEvent event) {
		listener.onAction(Action.SEAL);
	}
	
	@UiHandler("unseal")
	protected void onUnsealClick(ClickEvent event) {
		listener.onAction(Action.UNSEAL);
	}
	
	public void setListener(Listener listener) {
		this.listener = listener;
	}

	public void setEnabled(Action action, boolean enabled) {
		switch (action) {
			case SEAL: seal.setVisible(enabled); break;
			case UNSEAL: unseal.setVisible(enabled); break;
			case LOCK: lock.setVisible(enabled); break;
			case UNLOCK: unlock.setVisible(enabled); break;
		}
	}

}
