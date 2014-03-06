package org.cotrix.web.ingest.client.step.done;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(DoneStepViewImpl.class)
public interface DoneStepView {
	
	public void loadReport();
	Widget asWidget();
}
