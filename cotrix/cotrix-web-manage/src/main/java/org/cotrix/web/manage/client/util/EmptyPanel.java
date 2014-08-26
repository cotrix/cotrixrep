package org.cotrix.web.manage.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class EmptyPanel extends ResizeComposite {

	interface Binder extends UiBinder<Widget, EmptyPanel> { }
	
	@UiField TableCellElement messageCell;

	@Inject
	public EmptyPanel() {
		Binder uiBinder = GWT.create(Binder.class);
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setMessage(String message) {
		messageCell.setInnerText(message);
	}
}
