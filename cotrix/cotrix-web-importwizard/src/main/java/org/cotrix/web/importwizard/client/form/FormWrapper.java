package org.cotrix.web.importwizard.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class FormWrapper extends Composite {
	int index;
	public interface OnButtonClickHandler {
		boolean isFromValidated(FormWrapper sender);
		void onNextButtonClicked(FormWrapper sender);
		void onBackButtonClicked(FormWrapper sender);
	}
	

	private static FormWrapperUiBinder uiBinder = GWT
			.create(FormWrapperUiBinder.class);

	interface FormWrapperUiBinder extends UiBinder<Widget, FormWrapper> {
	}

	private OnButtonClickHandler mOnButtonClickHandler;

	@UiField
	FlowPanel contentPanel;

	@UiField
	Button nextButton;

	@UiHandler("nextButton")
	void handleNextButtonClick(ClickEvent e) {
		if(mOnButtonClickHandler.isFromValidated(this)){
			mOnButtonClickHandler.onNextButtonClicked(this);
		}
	}

	@UiField
	Label title;

	@UiField
	Button backButton;
	
	@UiHandler("backButton")
	void handleBackButtonClick(ClickEvent e) {
		mOnButtonClickHandler.onBackButtonClicked(this);
	}

	public void setOnButtonClicked(OnButtonClickHandler onButtonClickHandler){
		mOnButtonClickHandler = onButtonClickHandler;
	}
	
	public static FormWrapper getInstance(Widget content, String title,int index) {
		return new FormWrapper(content, title,index);
	}

	public static FormWrapper getInstanceWithoutBackButton(Widget content,
			String title,int index) {
		FormWrapper mFormWrapper = new FormWrapper(content, title,index);
		mFormWrapper.showBackButton(false);
		return mFormWrapper;
	}

	public static FormWrapper getInstanceWithoutNextButton(Widget content,
			String title,int index) {
		FormWrapper mFormWrapper = new FormWrapper(content, title,index);
		mFormWrapper.showNextButton(false);
		return mFormWrapper;

	}

	public void showBackButton(boolean visible) {
		backButton.setVisible(visible);
	}

	public void showNextButton(boolean visible) {
		nextButton.setVisible(visible);
	}
	
	public int getIndexInParent(){
		return this.index; 
	}
	
	public FormWrapper(Widget content, String mTitle,int index) {
		this.index = index;
		initWidget(uiBinder.createAndBindUi(this));
		contentPanel.add(content);
		title.setText(mTitle);
	}

}
