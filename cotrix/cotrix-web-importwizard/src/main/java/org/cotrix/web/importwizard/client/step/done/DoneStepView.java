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
	public void setSubTitle(String subtitle);
	public void loadReport();
	Widget asWidget();
}
