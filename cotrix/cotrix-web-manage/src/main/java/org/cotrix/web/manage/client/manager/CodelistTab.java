/**
 * 
 */
package org.cotrix.web.manage.client.manager;

import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistTab extends Composite implements HasCloseHandlers<Widget>{
	
	@UiTemplate("CodelistTab.ui.xml")
	interface CodeListTaUiBinder extends UiBinder<Widget, CodelistTab> {
	}

	private static CodeListTaUiBinder uiBinder = GWT.create(CodeListTaUiBinder.class);
	
	@UiField Label name;
	@UiField Label version;
	@UiField(provided=true)  PushButton close;

	public CodelistTab(String name, String version) {
		Image image = new Image(CotrixManagerResources.INSTANCE.close());
		image.getElement().getStyle().setVerticalAlign(VerticalAlign.TOP);		
		close = new PushButton(image);
		
		initWidget(uiBinder.createAndBindUi(this));
		this.name.setText(name);
		this.name.setTitle(name);
		this.version.setText(version);
		this.version.setTitle(version);
	}
	
	@UiHandler("close")
	protected void onCloseClick(ClickEvent clickEvent)
	{
		CloseEvent.fire(this, this);
	}

	@Override
	public HandlerRegistration addCloseHandler(CloseHandler<Widget> handler) {
		return addHandler(handler, CloseEvent.getType());
	}
}
