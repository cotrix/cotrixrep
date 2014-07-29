/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker.panel;

import org.cotrix.web.manage.client.codelist.codes.marker.parse.MarkerEvent;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MarkerEventPanel extends Composite {

	private static MarkerEventPanelUiBinder uiBinder = GWT.create(MarkerEventPanelUiBinder.class);

	interface MarkerEventPanelUiBinder extends UiBinder<Widget, MarkerEventPanel> {}
	
	interface Style extends CssResource {
		String icon();
	}
	
	@UiField Image icon;
	@UiField Label title;
	@UiField Label subTitle;
	@UiField Label description;
	@UiField Label timestamp;
	
	@UiField Style style;

	public MarkerEventPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setEvent(MarkerEvent event) {
		icon.setResource(CotrixManagerResources.INSTANCE.codes());
		icon.setStyleName(style.icon());
		
		title.setText(event.getTitle());
		subTitle.setText(event.getSubtitle());
		description.setText(event.getDescription());
		timestamp.setText(event.getUser()+" "+event.getTimestamp());
	}
}
