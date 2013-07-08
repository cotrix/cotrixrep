package org.cotrix.web.importwizard.client.step;

import java.util.List;

import org.cotrix.web.importwizard.client.step.summary.SummaryStepPresenterImpl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public interface FormWrapperView<T> {

	public interface Presenter<T> {
		void onNextButtonClicked();
		void onSaveButtonClicked();
		void onBackButtonClicked();
		void onUploadOtherButtonClicked();
		void onManageCodelistButtonClicked();
		void addForm(HasWidgets container);
	}
	void addForm();
	void setFormTitle(String title);
	void showBackButton(boolean isVisible);
	void showNextButton(boolean isVisible);
	void showSaveButton(boolean isVisible);
	void showUploadOtherButton(boolean isVisible);
	void showManageCodelistButton(boolean isVisible);
	void onNextButtonClicked(ClickEvent event);
	void onSaveButtonClicked(ClickEvent event);
	void onBackButtonClicked(ClickEvent event);
	void onUploadOtherButtonClicked(ClickEvent event);
	void onManageCodelistButtonClicked(ClickEvent event);

	Widget asWidget();
}
