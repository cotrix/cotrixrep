/**
 * 
 */
package org.cotrix.web.common.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Loader extends Composite {

	private static LoaderUiBinder uiBinder = GWT.create(LoaderUiBinder.class);

	interface LoaderUiBinder extends UiBinder<Widget, Loader> {
	}
	
	@UiField
	Label message;

	public Loader() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setMessage(String text) {
		message.setText(text);
	}

}
