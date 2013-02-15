package org.cotrix.web.importwizard.client.form;

import org.cotrix.web.importwizard.shared.ImportWizardModel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class DescribeHeaderForm extends Composite  implements CotrixForm{

	private static DescribeHeaderFormUiBinder uiBinder = GWT
			.create(DescribeHeaderFormUiBinder.class);

	interface DescribeHeaderFormUiBinder extends
			UiBinder<Widget, DescribeHeaderForm> {
	}

	public DescribeHeaderForm(ImportWizardModel importWizardModel) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public boolean isValidate() {
		// TODO Auto-generated method stub
		return false;
	}

}
