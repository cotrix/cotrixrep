package org.cotrix.web.manage.client.codelist.codes;


import org.cotrix.web.common.client.widgets.HasEditing;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(CodesPanelViewImpl.class)
public interface CodesPanelView {

	Widget asWidget();
	
	CodesToolbar getToolBar();
	
	CodesEditor getCodeListEditor();
	
	HasEditing getAttributesEditor();

	HasEditing getLinksEditor();

}
