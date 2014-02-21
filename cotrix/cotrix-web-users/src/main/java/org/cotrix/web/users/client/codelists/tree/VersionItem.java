/**
 * 
 */
package org.cotrix.web.users.client.codelists.tree;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class VersionItem extends Composite {
	
	interface VersionItemUiBinder extends UiBinder<Widget, VersionItem> {
	}

	private static VersionItemUiBinder uiBinder = GWT.create(VersionItemUiBinder.class);
	
	@UiField InlineLabel version;

	public VersionItem(String name, String version) {
		initWidget(uiBinder.createAndBindUi(this));
		this.version.setText(version);
	}

}
