package org.cotrix.web.importwizard.client;

import org.cotrix.web.importwizard.client.step.FormWrapperViewImpl;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public interface ImportWizardView {
	void setPresenter(Presenter presenter);

	public interface Presenter {
		void initForm(HasWidgets container);
	}
	void initProgressBarTracker();
	void initForm();
	void showNextStep(int index);
	void showPrevStep(int index);
	Widget asWidget();

}
