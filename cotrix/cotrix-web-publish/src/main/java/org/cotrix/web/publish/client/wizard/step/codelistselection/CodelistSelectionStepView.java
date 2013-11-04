package org.cotrix.web.publish.client.wizard.step.codelistselection;

import org.cotrix.web.share.shared.codelist.UICodelist;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface CodelistSelectionStepView {
	
	public interface Presenter {
		public void codelistSelected(UICodelist asset);
		public void codelistDetails(UICodelist asset);
		public void repositoryDetails(String repositoryId);
	}

	public void reset();
	void alert(String message);
	
	void setPresenter(Presenter presenter);
	Widget asWidget();
}
