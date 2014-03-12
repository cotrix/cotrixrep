package org.cotrix.web.publish.client.wizard.step.codelistselection;

import org.cotrix.web.common.shared.codelist.UICodelist;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
@ImplementedBy(CodelistSelectionStepViewImpl.class)
public interface CodelistSelectionStepView {
	
	public interface Presenter {
		public void codelistSelected(UICodelist codelist);
		public void codelistDetails(UICodelist codelist);
	}

	public void reset();
	void alert(String message);
	
	void setPresenter(Presenter presenter);
	Widget asWidget();
}
