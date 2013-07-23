package org.cotrix.web.importwizard.client.util;

import org.cotrix.web.importwizard.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public class ProgressDialog extends DialogBox {
	private static final Binder binder = GWT.create(Binder.class);
	interface Binder extends UiBinder<Widget, ProgressDialog> {}

	public ProgressDialog() {

		Resources.INSTANCE.css().ensureInjected();
		setGlassStyleName(Resources.INSTANCE.css().glassPanel());
		
		setModal(true);
		setGlassEnabled(true);
		setAnimationEnabled(true);
		
		setWidget(binder.createAndBindUi(this));
	}
}
