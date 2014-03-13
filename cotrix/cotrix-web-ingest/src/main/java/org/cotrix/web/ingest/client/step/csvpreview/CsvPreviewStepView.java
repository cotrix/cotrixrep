package org.cotrix.web.ingest.client.step.csvpreview;

import java.util.List;

import org.cotrix.web.common.shared.CsvConfiguration;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
@ImplementedBy(CsvPreviewStepViewImpl.class)
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
