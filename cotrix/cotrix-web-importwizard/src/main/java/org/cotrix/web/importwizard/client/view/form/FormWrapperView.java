package org.cotrix.web.importwizard.client.view.form;

import java.util.List;

import org.cotrix.web.importwizard.client.presenter.SummaryFormPresenterImpl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public interface FormWrapperView<T> {

	public interface Presenter<T> {
		void onNextButtonClicked();
		void onSaveButtonClicked();
		void onBackButtonClicked();
		void addForm(HasWidgets container);
	}
	void addForm();
	void setFormTitle(String title);
	void showBackButton(boolean isVisible);
	void showNextButton(boolean isVisible);
	void showSaveButton(boolean isVisible);
	void onNextButtonClicked(ClickEvent event);
	void onSaveButtonClicked(ClickEvent event);
	void onBackButtonClicked(ClickEvent event);

	Widget asWidget();
}
