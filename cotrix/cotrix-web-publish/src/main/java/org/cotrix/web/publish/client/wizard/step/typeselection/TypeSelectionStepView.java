package org.cotrix.web.publish.client.wizard.step.typeselection;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(TypeSelectionStepViewImpl.class)
public interface TypeSelectionStepView {
	
	public interface Presenter {
		void onSDMXButtonClick();
		void onCSVButtonClick();
		void onCometButtonClick();
	}
	
	void alert(String message);
	void setPresenter(Presenter presenter);
	
	Widget asWidget();
}
