package org.cotrix.web.importwizard.client.step.csvpreview;

import java.util.List;

import org.cotrix.web.share.shared.CsvParserConfiguration;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface CsvPreviewStepView {
	public interface Presenter {
		void onShowCsvConfigurationButtonClicked();
		void onCsvConfigurationEdited(CsvParserConfiguration configuration);
	}

	public List<String> getHeaders();
	
	public void showCsvConfigurationDialog();
	public void setCsvParserConfiguration(CsvParserConfiguration configuration);
	
	void alert(String message);

	void setPresenter(Presenter presenter);
	
	Widget asWidget();
}
