package org.cotrix.web.importwizard.client.step.preview;

import java.util.List;

import org.cotrix.web.importwizard.shared.CsvParserConfiguration;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface PreviewStepView {
	public interface Presenter {
		void onShowCsvConfigurationButtonClicked();
		void onCsvConfigurationEdited(CsvParserConfiguration configuration);
	}

	public List<String> getEditedHeaders();
	
	public void cleanPreviewGrid();
	public void setupEditableHeader(int numColumns);
	public void setupStaticHeader(List<String> headers);
	public void setData(List<List<String>> rows);
	
	public void showCsvConfigurationButton();
	public void hideCsvConfigurationButton();
	
	public void showCsvConfigurationDialog();
	public void setCsvParserConfiguration(CsvParserConfiguration configuration);
	
	void alert(String message);

	void setPresenter(Presenter presenter);
	
	Widget asWidget();
}
