package org.cotrix.web.importwizard.client.step.preview;

import java.util.ArrayList;

import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.client.wizard.WizardStepConfiguration;
import org.cotrix.web.share.shared.CSVFile;
import org.cotrix.web.share.shared.CotrixImportModelController;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PreviewStepPresenterImpl extends AbstractWizardStep implements PreviewStepPresenter {

	private final PreviewStepView view;
	private  CotrixImportModelController model;
	
	@Inject
	public PreviewStepPresenterImpl(PreviewStepView view, CotrixImportModelController model) {
		super("preview", "Select Header", "Select Header", NavigationButtonConfiguration.DEFAULT_BACKWARD, NavigationButtonConfiguration.DEFAULT_FORWARD);
		this.view = view;
		this.model = model;
		this.view.setPresenter(this);
		this.model.addOnFileChangeHandler(this);
	}

	public void go(HasWidgets container) {
		//container.clear();
		container.add(view.asWidget());
	}

	public boolean isComplete() {
		ArrayList<String> headers = view.getHeaders();
		int columnCount = this.model.getCsvFile().getHeader().length;
		if(headers.size() != columnCount){
			view.alert("Please define all header");
		}else{
			model.getCsvFile().setHeader(headers.toArray(new String[0]));
		}
		return (headers.size() == columnCount)?true:false;
	}

	public void onCheckBoxChecked(boolean isChecked) {
		view.showHeaderForm(!isChecked);
	}

	public void onFileChange(CSVFile csvFile) {
		view.setData(csvFile.getHeader(),csvFile.getData());
	}
}
