package org.cotrix.web.share.client.widgets;

import org.cotrix.web.share.client.resources.CommonCss;
import org.cotrix.web.share.client.resources.CommonResources;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ConfirmDialog extends PopupPanel implements HasSelectionHandlers<org.cotrix.web.share.client.widgets.ConfirmDialog.DialogButton> {
	
	public enum DialogButton {CONTINUE, CANCEL};
	
	protected HTML message;

	public ConfirmDialog() {

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
		
		Button continueButton = new Button("Continue");
		continueButton.setStyleName(style.blueButton());
		continueButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				SelectionEvent.fire(ConfirmDialog.this, DialogButton.CONTINUE);
				hide();
			}
		});
		
		Button cancelButton = new Button("Cancel");
		cancelButton.setStyleName(style.grayButton());
		cancelButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				SelectionEvent.fire(ConfirmDialog.this, DialogButton.CANCEL);
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
				if (event.isAutoClosed()) SelectionEvent.fire(ConfirmDialog.this, DialogButton.CANCEL);
				
			}
		});
	}

	public void setMessage(String message) {
		this.message.setHTML(message);
	}
	
	public void center(String message) {
		setMessage(message);
		center();
	}

	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<org.cotrix.web.share.client.widgets.ConfirmDialog.DialogButton> handler) {

		return addHandler(handler, SelectionEvent.getType());
	}
}
