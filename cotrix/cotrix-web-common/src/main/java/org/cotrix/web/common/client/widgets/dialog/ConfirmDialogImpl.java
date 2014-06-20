package org.cotrix.web.common.client.widgets.dialog;

import org.cotrix.web.common.client.resources.CommonCss;
import org.cotrix.web.common.client.resources.CommonResources;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class ConfirmDialogImpl extends PopupPanel implements ConfirmDialog {
	
	private ConfirmDialogListener listener;
	private HTML message;
	private Button continueButton;
	private Button cancelButton;

	public ConfirmDialogImpl() {

		CommonCss style = CommonResources.INSTANCE.css();
		setModal(true);
		setAutoHideEnabled(true);
		setWidth("330px");

		VerticalPanel content = new VerticalPanel();
		content.setStyleName(style.errorPanel());
		content.setSpacing(5);
		
		FlowPanel messageContainer = new FlowPanel();
		messageContainer.setStyleName(style.errorMessageContainer());
		
		message = new HTML();
		message.setStyleName(style.errorMessage());
		messageContainer.add(message);
		content.add(messageContainer);
		
		continueButton = new Button("Continue");
		continueButton.setStyleName(style.blueButton());
		continueButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireClick(DialogButton.CONTINUE);
				hide();
			}
		});
		
		cancelButton = new Button("Cancel");
		cancelButton.setStyleName(style.grayButton());
		cancelButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireClick(DialogButton.CANCEL);
				hide();
			}
		});
		
		HorizontalPanel buttonBar = new HorizontalPanel();
		buttonBar.setWidth("100%");
		buttonBar.setHeight("45px");
		buttonBar.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		buttonBar.add(continueButton);
		buttonBar.add(cancelButton);
		content.add(buttonBar);

		add(content);
		
		addCloseHandler(new CloseHandler<PopupPanel>() {
			
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				if (event.isAutoClosed()) fireClick(DialogButton.CANCEL);
				
			}
		});
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void center(String message, ConfirmDialogListener listener, DialogButton ... disabledButtons) {
		this.listener = listener;
		this.message.setHTML(message);
		enableAllButtons();
		updateButtons(disabledButtons);
		center();
	}
	
	private void updateButtons(DialogButton[] disabledButtons) {
		if (disabledButtons.length == 0) return;
		for (DialogButton button:disabledButtons) enableButton(button, false);
	}
	
	private void enableAllButtons() {
		for (DialogButton button:DialogButton.values()) enableButton(button, true);
	}
	
	private void enableButton(DialogButton button, boolean enable) {
		switch (button) {
			case CANCEL: cancelButton.setVisible(enable); break;
			case CONTINUE: continueButton.setVisible(enable); break;
		}
	}
	
	private void fireClick(DialogButton button) {
		if (listener!=null) listener.onButtonClick(button);
	}

	@Override
	public void warning(String message) {
		center(message, null, DialogButton.CONTINUE);
	}
}
