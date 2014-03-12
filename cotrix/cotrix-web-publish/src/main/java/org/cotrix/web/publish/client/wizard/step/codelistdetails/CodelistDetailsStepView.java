package org.cotrix.web.publish.client.wizard.step.codelistdetails;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
@ImplementedBy(CodelistDetailsStepViewImpl.class)
public interface CodelistDetailsStepView {

	Widget asWidget();

	void setName(String name);

	void setVersion(String version);

	void setState(String state);

	void setAttributesVisible(boolean visible);

	void clearAttributes();

	void addAttribute(String name, String value);
}
