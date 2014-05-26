package org.cotrix.web.manage.client.codelist;


import org.cotrix.web.common.client.widgets.HasEditing;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(CodelistPanelViewImpl.class)
public interface CodelistPanelView {

	Widget asWidget();
	
	CodelistToolbar getToolBar();
	
	CodelistEditor getCodeListEditor();
	
	HasEditing getMetadataEditor();
	
	HasEditing getAttributesEditor();
	
	HasEditing getLinkTypesEditor();

	HasEditing getLinksEditor();

	HasEditing getAttributeTypesPanel();

}
