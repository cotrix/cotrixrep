/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata;

import org.cotrix.web.common.client.error.ErrorManager;
import org.cotrix.web.common.client.widgets.AlertDialog;
import org.cotrix.web.common.client.widgets.LoadingPanel;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.manage.client.codelist.NewStateEvent;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.di.CurrentCodelist;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
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
	
	@UiField Label name;
	@UiField InlineLabel version;
	@UiField Label state;
	
	@Inject
	private AlertDialog alertDialog;
	
	@Inject
	private ErrorManager errorManager;
	
	@Inject @CurrentCodelist
	private UICodelist codelist;

	@Inject
	protected void init(ProfilePanelUiBinder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		setAnimated(true);
		name.setText(codelist.getName().getLocalPart());
		version.setText(codelist.getVersion());
		state.setText(codelist.getState()!=null?codelist.getState().toString():"");
	}
	
	@EventHandler
	void onNewState(NewStateEvent event) {
		state.setText(event.getState());
	}
	
	@Inject
	protected void bind(@CodelistBus EventBus bus, ProfilePanelEventBinder eventBinder) {
		eventBinder.bindEventHandlers(this, bus);
	}

}
