package org.cotrix.web.importwizard.client.step.preview;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface PreviewStepView {
	public interface Presenter {
		void onShowCsvConfigurationButtonClicked();
	}

	public List<String> getEditedHeaders();
	
	public void cleanPreviewGrid();
	public void setupEditableHeader(int numColumns);
	public void setupStaticHeader(List<String> headers);
	public void setData(List<List<String>> rows);
	
	public void showCsvConfigurationButton();
	public void hideCsvConfigurationButton();
	
	void alert(String message);

	
	void setPresenter(Presenter presenter);
	
	Widget asWidget();
}
