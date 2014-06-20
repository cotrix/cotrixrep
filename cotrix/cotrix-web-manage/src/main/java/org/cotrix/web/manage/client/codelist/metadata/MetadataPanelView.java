package org.cotrix.web.manage.client.codelist.metadata;


import org.cotrix.web.common.client.widgets.HasEditing;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(MetadataPanelViewImpl.class)
public interface MetadataPanelView {

	Widget asWidget();
	
	MetadataToolbar getToolBar();
	
	HasEditing getAttributesEditor();
	
	HasEditing getLinkTypesEditor();

	HasEditing getAttributeTypesPanel();

}
