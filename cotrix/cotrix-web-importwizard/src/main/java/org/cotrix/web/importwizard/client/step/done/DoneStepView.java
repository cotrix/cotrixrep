package org.cotrix.web.importwizard.client.step.done;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface DoneStepView {
	
	public interface Presenter {
	}
	
	void setTitle(String title);
	void setMessage(String message);
	Widget asWidget();
	void mask();
	void unmask();
}
