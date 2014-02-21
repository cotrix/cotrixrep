/**
 * 
 */
package org.cotrix.web.users.client.preferences;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class PreferencesPanel extends Composite {

	interface PreferencesPanelUiBinder extends UiBinder<Widget, PreferencesPanel> {
	}

	@Inject
	protected void init(PreferencesPanelUiBinder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
