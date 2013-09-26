/**
 * 
 */
package org.cotrix.web.codelistmanager.client.codelist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListToolbar extends Composite {
	
	@UiTemplate("CodeListToolbar.ui.xml")
	interface CodelistToolbarUiBinder extends UiBinder<Widget, CodeListToolbar> {}
	private static CodelistToolbarUiBinder uiBinder = GWT.create(CodelistToolbarUiBinder.class);
	
	@UiField Button metadata;
	
	public CodeListToolbar() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	boolean meta = true;
	
	@UiHandler("metadata")
	protected void onMetadataClick(ClickEvent event)
	{
		this.meta = !meta;
		metadata.setText(meta?"Metadata":"Data");
		if (clickListener!=null) clickListener.onClick();
	}
	
	protected ClickListener clickListener;
	
	
	
	/**
	 * @param clickListener the clickListener to set
	 */
	public void setClickListener(ClickListener clickListener) {
		this.clickListener = clickListener;
	}



	interface ClickListener {
		public void onClick();
	}

}
