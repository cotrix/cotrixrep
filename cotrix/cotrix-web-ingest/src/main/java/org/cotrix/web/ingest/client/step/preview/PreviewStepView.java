package org.cotrix.web.ingest.client.step.preview;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
@ImplementedBy(PreviewStepViewImpl.class)
public interface PreviewStepView {
	
	public interface Presenter {
	}

	void setPresenter(Presenter presenter);
	
	void updatePreview();
	
	Widget asWidget();
}
