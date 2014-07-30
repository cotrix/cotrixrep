/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker.panel;

import org.cotrix.web.manage.client.codelist.codes.marker.event.MarkerEvent;
import org.cotrix.web.manage.client.codelist.codes.marker.event.MarkerEventIconProvider;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
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
	@UiField TableRowElement subTitleRow;
	@UiField Label subTitle;
	@UiField Label description;
	@UiField Label timestamp;
	
	@UiField Style style;

	public MarkerEventPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setEvent(MarkerEvent event) {
		ImageResource eventIcon = MarkerEventIconProvider.getIcon(event.getType());
		icon.setResource(eventIcon);
		icon.setStyleName(style.icon());
		
		title.setText(event.getTitle());
		
		boolean hideSubTitleRow = event.getSubtitle() == null || event.getSubtitle().isEmpty();
		subTitleRow.getStyle().setProperty("display", hideSubTitleRow?"none":"table-row");
		subTitle.setText(event.getSubtitle());
		
		description.setText(event.getDescription());
		timestamp.setText(event.getUser()+" "+event.getTimestamp());
	}
}
