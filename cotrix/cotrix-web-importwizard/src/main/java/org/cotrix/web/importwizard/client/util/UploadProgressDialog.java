package org.cotrix.web.importwizard.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class UploadProgressDialog extends PopupPanel {

	private static UploadProgressDialogUiBinder uiBinder = GWT
			.create(UploadProgressDialogUiBinder.class);

	interface UploadProgressDialogUiBinder extends
			UiBinder<Widget, UploadProgressDialog> {
	}

	public UploadProgressDialog() {
		setWidget(uiBinder.createAndBindUi(this));
	}

}
