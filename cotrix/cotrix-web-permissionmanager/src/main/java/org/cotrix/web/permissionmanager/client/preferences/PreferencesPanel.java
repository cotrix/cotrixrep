/**
 * 
 */
package org.cotrix.web.permissionmanager.client.preferences;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PreferencesPanel extends Composite {

	private static PreferencesPanelUiBinder uiBinder = GWT
			.create(PreferencesPanelUiBinder.class);

	interface PreferencesPanelUiBinder extends
			UiBinder<Widget, PreferencesPanel> {
	}

	
	public PreferencesPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
