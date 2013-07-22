package org.cotrix.web.importwizard.client.step.summary;

import java.util.List;

import org.cotrix.web.importwizard.shared.ColumnDefinition;
import org.cotrix.web.importwizard.shared.ImportMetadata;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface SummaryStepView {
	
	public interface Presenter {
		
	}

	public void setColumns(List<ColumnDefinition> columns);
	void setMetadata(ImportMetadata metadata);
	
	void alert(String message);

	Widget asWidget();
}
