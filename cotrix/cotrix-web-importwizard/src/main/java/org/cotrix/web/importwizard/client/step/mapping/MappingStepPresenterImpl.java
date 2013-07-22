package org.cotrix.web.importwizard.client.step.mapping;

import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.share.shared.CSVFile;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MappingStepPresenterImpl extends AbstractWizardStep implements MappingStepPresenter {

	private final MappingStepFormView view;
	
	@Inject
	public MappingStepPresenterImpl(MappingStepFormView view){
		super("mapping", "Define Type", "Define Type", NavigationButtonConfiguration.DEFAULT_BACKWARD, NavigationButtonConfiguration.DEFAULT_FORWARD);
		this.view = view;
		this.view.setPresenter(this);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}
	
	public boolean isComplete() {
		boolean validateResult = false;
		/*int counter = 0;
		ArrayList<HeaderType> types = view.getHeaderTypes();
		for (HeaderType type : types) {
			if(type.getValue()!= null && type.getValue().equals("Code")){
				counter++;
			}
		}
		if(counter == 1){
			validateResult = true;
			model.setType(types);
		}else{
			view.setStyleError();
			AlertDialog dialog = new AlertDialog();
			dialog.setMessage("You can assign only one code.");
			dialog.show();
		}*/
		return validateResult;
	}
	
	public void onFileChange(CSVFile csvFile) {
		view.setData(csvFile.getHeader());
	}
}
