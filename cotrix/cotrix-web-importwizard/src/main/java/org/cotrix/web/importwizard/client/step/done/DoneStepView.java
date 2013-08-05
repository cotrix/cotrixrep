package org.cotrix.web.importwizard.client.step.done;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface DoneStepView {
	
	public interface Presenter {
		public void importButtonClicked();
		public void manageButtonClicked();
	}
	
	void setPresenter(Presenter presenter);
	void setStepTitle(String title);
	public void setReportPanelVisible(boolean visible);
	void setMessage(String message);
	Widget asWidget();
}
