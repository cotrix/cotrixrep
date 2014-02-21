package org.cotrix.web.importwizard.client.step.csvpreview;

import java.util.List;

import org.cotrix.web.share.shared.CsvConfiguration;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface CsvPreviewStepView {
	public interface Presenter {
		void onCsvConfigurationEdited(CsvConfiguration configuration);
	}

	public List<String> getHeaders();
	
	public void setCsvParserConfiguration(CsvConfiguration configuration);
	
	void alert(String message);

	void setPresenter(Presenter presenter);
	
	Widget asWidget();
}
