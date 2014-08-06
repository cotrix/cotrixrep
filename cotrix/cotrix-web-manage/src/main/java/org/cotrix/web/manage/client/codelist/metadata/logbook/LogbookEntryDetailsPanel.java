/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.logbook;

import org.cotrix.web.common.shared.codelist.linkdefinition.CodeNameValue;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanel.ItemView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LogbookEntryDetailsPanel extends Composite implements ItemView {

	public static final String CODE_NAME_VALUE_TYPE = Document.get().createUniqueId();
	public static final CodeNameValue CODE_NAME_TYPE = new CodeNameValue();

	private static LogbookEntryPanelUiBinder uiBinder = GWT.create(LogbookEntryPanelUiBinder.class);

	interface LogbookEntryPanelUiBinder extends UiBinder<Widget, LogbookEntryDetailsPanel> {}

	@UiField Label eventLabel;
	@UiField Label timestampLabel;
	@UiField Label descriptionLabel;
	@UiField Label userLabel;
	
	public LogbookEntryDetailsPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setEvent(String event) {
		this.eventLabel.setText(event);
	}

	public void setTimestamp(String timestamp) {
		this.timestampLabel.setText(timestamp);
	}

	public void setDescription(String description) {
		this.descriptionLabel.setText(description);
	}

	public void setUser(String user) {
		this.userLabel.setText(user);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Void> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}
}
