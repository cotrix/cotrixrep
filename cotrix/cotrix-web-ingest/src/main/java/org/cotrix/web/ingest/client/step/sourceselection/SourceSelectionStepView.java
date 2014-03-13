package org.cotrix.web.ingest.client.step.sourceselection;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(SourceSelectionStepViewImpl.class)
public interface SourceSelectionStepView {
	
	public interface Presenter {
		void onCloudButtonClick();
		void onLocalButtonClick();
	}
	
	void alert(String message);
	void setPresenter(Presenter presenter);
	
	Widget asWidget();
}
