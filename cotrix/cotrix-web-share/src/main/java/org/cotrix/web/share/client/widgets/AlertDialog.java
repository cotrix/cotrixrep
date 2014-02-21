package org.cotrix.web.share.client.widgets;

import org.cotrix.web.share.client.resources.CommonCss;
import org.cotrix.web.share.client.resources.CommonResources;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AlertDialog extends PopupPanel {

	public static AlertDialog INSTANCE = new AlertDialog(); 
	
	protected HTML message;
	protected HTML details;
	protected DisclosurePanel detailsPanel;

	private AlertDialog() {

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
		
		
		detailsPanel = new DisclosurePanel("details");
		detailsPanel.setWidth("325px");
		detailsPanel.setAnimationEnabled(true);
		
		detailsPanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				center();
			}
		});
		
		detailsPanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				center();
			}
		});
		
		details = new HTML();
		ScrollPanel detailsScrollablePanel = new ScrollPanel(details);
		double maxHeight = Window.getClientHeight()/2 - 100;
		detailsScrollablePanel.getElement().getStyle().setProperty("maxHeight", maxHeight, Unit.PX);
		detailsPanel.setContent(detailsScrollablePanel);
		
		content.add(detailsPanel);

		add(content);
		
	}

	public void setMessage(String message) {
		this.message.setHTML(message);
		detailsPanel.setVisible(false);
	}
	
	public void setMessage(String message, String details) {
		this.message.setHTML(message);
		this.details.setHTML(details);
		detailsPanel.setVisible(true);
	}
	
	public void center(String message) {
		setMessage(message);
		center();
	}
	
	public void center(String message, String details) {
		setMessage(message, details);
		center();
	}
}
