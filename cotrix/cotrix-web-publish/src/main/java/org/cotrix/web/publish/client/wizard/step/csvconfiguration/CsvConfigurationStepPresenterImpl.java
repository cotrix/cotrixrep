package org.cotrix.web.publish.client.wizard.step.csvconfiguration;

import org.cotrix.web.publish.client.event.CsvWriterConfigurationUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.share.client.wizard.step.AbstractVisualWizardStep;
import org.cotrix.web.share.shared.CsvConfiguration;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvConfigurationStepPresenterImpl extends AbstractVisualWizardStep implements CsvConfigurationStepPresenter {

	private final CsvConfigurationStepView view;
	protected EventBus publishBus;
	protected boolean headerRequired = false;
	
	@Inject
	public CsvConfigurationStepPresenterImpl(CsvConfigurationStepView view, @PublishBus EventBus publishBus) {
		super("csv-preview", TrackerLabels.CSVCONFIG, "Does it look right?", "Adjust the parameters until it does.", PublishWizardStepButtons.BACKWARD, PublishWizardStepButtons.FORWARD);
		this.view = view;
		this.view.setPresenter(this);
		
		this.publishBus = publishBus;
		bind();
	}
	
	protected void bind()
	{
		publishBus.addHandler(CsvWriterConfigurationUpdatedEvent.TYPE, new CsvWriterConfigurationUpdatedEvent.CsvWriterConfigurationUpdatedHandler() {
			
			@Override
			public void onCsvWriterConfigurationUpdated(
					CsvWriterConfigurationUpdatedEvent event) {
				if (event.getSource() != CsvConfigurationStepPresenterImpl.this) view.setCsvParserConfiguration(event.getConfiguration());
				
			}
		});
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		return true;
	}

	@Override
	public void onCsvConfigurationEdited(CsvConfiguration configuration) {
		publishBus.fireEventFromSource(new CsvWriterConfigurationUpdatedEvent(configuration), this);
	}

}
