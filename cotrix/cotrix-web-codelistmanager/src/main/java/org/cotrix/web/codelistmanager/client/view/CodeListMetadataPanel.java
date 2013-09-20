package org.cotrix.web.codelistmanager.client.view;

import org.cotrix.web.codelistmanager.client.resources.CotrixManagerResources;
import org.cotrix.web.share.shared.UICodelist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListMetadataPanel extends ResizeComposite {

	@UiTemplate("CodeListMetadataPanel.ui.xml")
	interface CodeListMetadataPanelUiBinder extends
	UiBinder<Widget, CodeListMetadataPanel> {
	}

	private static CodeListMetadataPanelUiBinder uiBinder = GWT.create(CodeListMetadataPanelUiBinder.class);

	CotrixManagerResources resources = GWT.create(CotrixManagerResources.class);
	
	@UiField Label name;

	public CodeListMetadataPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setCodelist(UICodelist codelist)
	{
		name.setText(codelist.getName());
		
	}
}
