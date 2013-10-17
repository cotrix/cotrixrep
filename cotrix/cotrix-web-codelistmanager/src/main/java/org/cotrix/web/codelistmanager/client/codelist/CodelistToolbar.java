/**
 * 
 */
package org.cotrix.web.codelistmanager.client.codelist;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistToolbar extends Composite {
	
	@UiTemplate("CodelistToolbar.ui.xml")
	interface CodelistToolbarUiBinder extends UiBinder<Widget, CodelistToolbar> {}
	private static CodelistToolbarUiBinder uiBinder = GWT.create(CodelistToolbarUiBinder.class);
	
	@UiField Button allColumns;
	@UiField Button allNormals;
	
	@UiField Button lock;
	@UiField Button unlock;
	@UiField Button seal;
	
	public CodelistToolbar() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/**
	 * @return the allColumns
	 */
	public Button getAllColumns() {
		return allColumns;
	}

	/**
	 * @return the allNormals
	 */
	public Button getAllNormals() {
		return allNormals;
	}

	/**
	 * @param allNormals the allNormals to set
	 */
	public void setAllNormals(Button allNormals) {
		this.allNormals = allNormals;
	}

	public Button getLockButton()
	{
		return lock;
	}

	public Button getUnlockButton()
	{
		return unlock;
	}

	public Button getSealButton()
	{
		return seal;
	}

}
