package org.cotrix.web.publish.client.wizard.step.csvconfiguration;

import org.cotrix.web.share.shared.CsvConfiguration;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface CsvConfigurationStepView {
	public interface Presenter {
		void onCsvConfigurationEdited(CsvConfiguration configuration);
	}

	public void setCsvParserConfiguration(CsvConfiguration configuration);
	
	void alert(String message);

	void setPresenter(Presenter presenter);
	
	Widget asWidget();
}
