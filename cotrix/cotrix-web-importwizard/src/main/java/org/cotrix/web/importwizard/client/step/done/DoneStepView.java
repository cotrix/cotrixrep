package org.cotrix.web.importwizard.client.step.done;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface DoneStepView {
	
	public interface Presenter {
	}
	
	void setPresenter(Presenter presenter);
	void setDoneTitle(String title);
	void setWarningMessage(String message);
	Widget asWidget();
}
