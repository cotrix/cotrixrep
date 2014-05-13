package org.cotrix.web.publish.client.wizard.step.formatselection;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(FormatSelectionStepViewImpl.class)
public interface FormatSelectionStepView {
	
	public interface Presenter {
		void onSDMXButtonClick();
		void onCSVButtonClick();
		void onCometButtonClick();
	}

	void setPresenter(Presenter presenter);
	
	public void setCSVVisible(boolean visible);
	public void setSDMXVisible(boolean visible);
	public void setCometVisible(boolean visible);
	
	Widget asWidget();
}
