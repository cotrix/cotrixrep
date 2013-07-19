package org.cotrix.web.importwizard.client.step.preview;

import java.util.List;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.PreviewDataUpdatedEvent;
import org.cotrix.web.importwizard.client.event.PreviewDataUpdatedEvent.PreviewDataUpdatedHandler;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.shared.CodeListPreviewData;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import static org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration.*;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PreviewStepPresenterImpl extends AbstractWizardStep implements PreviewStepPresenter, PreviewDataUpdatedHandler {

	private final PreviewStepView view;
	protected EventBus importEventBus;
	
	@Inject
	public PreviewStepPresenterImpl(PreviewStepView view, @ImportBus EventBus importEventBus) {
		super("preview", "Preview", "CodeList Preview", DEFAULT_BACKWARD, DEFAULT_FORWARD);
		this.view = view;
		this.view.setPresenter(this);
		
		importEventBus.addHandler(PreviewDataUpdatedEvent.TYPE, this);
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}
	
	public void setPreviewData(List<String> header, int numColumns, List<List<String>> data)
	{
		view.cleanPreviewGrid();
		if (header!=null) view.setupStaticHeader(header);
		else view.setupEditableHeader(numColumns);
		view.setData(data);
	}

	public boolean isComplete() {
		/*ArrayList<String> headers = view.getHeaders();
		int columnCount = this.model.getCsvFile().getHeader().length;
		if(headers.size() != columnCount){
			view.alert("Please define all header");
		}else{
			model.getCsvFile().setHeader(headers.toArray(new String[0]));
		}
		return (headers.size() == columnCount)?true:false;*/
		return false;
	}

	@Override
	public void onShowCsvConfigurationButtonClicked() {
		
	}

	@Override
	public void onPreviewDataUpdated(PreviewDataUpdatedEvent event) {
		CodeListPreviewData previewData = event.getPreviewData();
		setPreviewData(previewData.getHeader(), previewData.getColumnsCount(), previewData.getData());
		
	}
}
