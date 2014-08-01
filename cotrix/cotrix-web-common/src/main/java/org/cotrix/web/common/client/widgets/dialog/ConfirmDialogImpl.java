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
	private HorizontalPanel buttonBar;

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
		
		buttonBar = new HorizontalPanel();
		buttonBar.setWidth("100%");
		buttonBar.setHeight("45px");
		buttonBar.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		content.add(buttonBar);

		add(content);
		
		addCloseHandler(new CloseHandler<PopupPanel>() {
			
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				if (event.isAutoClosed()) fireClick(DialogButtonDefaultSet.CANCEL);
				
			}
		});
	}
	

	@Override
	public void center(String message, ConfirmDialogListener listener) {
		center(message, listener, DialogButtonDefaultSet.values());
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void center(String message, ConfirmDialogListener listener, DialogButton ... buttons) {
		this.listener = listener;
		this.message.setHTML(message);
		buildButtonBar(buttons);
		center();
	}

	private void buildButtonBar(DialogButton[] buttons) {
		buttonBar.clear();
		for (final DialogButton button:buttons) {
			Button dialogButton = new Button(button.getLabel());
			dialogButton.setStyleName(button.getStyleName());
			dialogButton.setWidth(button.getWidth()+"px");
			dialogButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					fireClick(button);
					hide();
				}
			});
			buttonBar.add(dialogButton);
		}
	}
	
	private void fireClick(DialogButton button) {
		if (listener!=null) listener.onButtonClick(button);
	}

	@Override
	public void warning(String message) {
		center(message, null, DialogButtonDefaultSet.CONTINUE);
	}

}
