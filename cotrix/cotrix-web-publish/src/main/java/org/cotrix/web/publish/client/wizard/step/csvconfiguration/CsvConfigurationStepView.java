package org.cotrix.web.publish.client.wizard.step.csvconfiguration;

import org.cotrix.web.share.shared.CsvParserConfiguration;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface CsvConfigurationStepView {
	public interface Presenter {
		void onShowCsvConfigurationButtonClicked();
		void onCsvConfigurationEdited(CsvParserConfiguration configuration);
	}

	public void showCsvConfigurationDialog();
	public void setCsvParserConfiguration(CsvParserConfiguration configuration);
	
	void alert(String message);

	void setPresenter(Presenter presenter);
	
	Widget asWidget();
}
