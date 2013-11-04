package org.cotrix.web.publish.client.wizard.step.codelistdetails;

import org.cotrix.web.share.shared.codelist.CodelistMetadata;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface CodelistDetailsStepView {
	
	public void setCodelist(CodelistMetadata codelist);

	Widget asWidget();
}
