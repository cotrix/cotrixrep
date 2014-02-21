package org.cotrix.web.manage.client.codelist;


import org.cotrix.web.common.client.widgets.HasEditing;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodelistPanelView {
	public interface Presenter {

	}

	Widget asWidget();
	
	CodelistToolbar getToolBar();
	
	CodelistEditor getCodeListEditor();
	
	HasEditing getMetadataEditor();
	
	HasEditing getAttributesEditor();

}
