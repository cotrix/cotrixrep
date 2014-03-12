package org.cotrix.web.publish.client.wizard.step.csvconfiguration;

import org.cotrix.web.common.shared.CsvConfiguration;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
@ImplementedBy(CsvConfigurationStepViewImpl.class)
public interface CsvConfigurationStepView {

	public void setCsvWriterConfiguration(CsvConfiguration configuration);
	public CsvConfiguration getCsvWriterConfiguration();
	
	void alert(String message);
	
	Widget asWidget();
}
