package org.cotrix.web.publish.client.wizard.step.csvconfiguration;

import org.cotrix.web.common.shared.CsvConfiguration;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface CsvConfigurationStepView {
	public interface Presenter {
	}

	public void setCsvWriterConfiguration(CsvConfiguration configuration);
	public CsvConfiguration getCsvWriterConfiguration();
	
	void alert(String message);

	void setPresenter(Presenter presenter);
	
	Widget asWidget();
}
