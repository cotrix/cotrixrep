package org.cotrix.web.publish.client.wizard.step.csvconfiguration;

import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.share.client.wizard.step.AbstractVisualWizardStep;
import org.cotrix.web.share.shared.CsvParserConfiguration;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvConfigurationStepPresenterImpl extends AbstractVisualWizardStep implements CsvConfigurationStepPresenter {

	private final CsvConfigurationStepView view;
	protected EventBus importEventBus;
	protected boolean headerRequired = false;
	
	@Inject
	public CsvConfigurationStepPresenterImpl(CsvConfigurationStepView view, @PublishBus EventBus importEventBus) {
		super("csv-preview", TrackerLabels.CSVCONFIG, "Does it look right?", "Adjust the parameters until it does.", PublishWizardStepButtons.BACKWARD, PublishWizardStepButtons.FORWARD);
		this.view = view;
		this.view.setPresenter(this);
		
		this.importEventBus = importEventBus;
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		return true;
	}

	@Override
	public void onShowCsvConfigurationButtonClicked() {
		view.showCsvConfigurationDialog();
	}

	@Override
	public void onCsvConfigurationEdited(CsvParserConfiguration configuration) {
		// TODO Auto-generated method stub
		
	}

}
