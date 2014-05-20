package org.cotrix.web.publish.client.wizard.step.csvconfiguration;

import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.publish.client.event.ItemUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.wizard.client.step.AbstractVisualWizardStep;
import org.cotrix.web.wizard.client.step.VisualWizardStep;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CsvConfigurationStepPresenter extends AbstractVisualWizardStep implements VisualWizardStep {

	@Inject
	private CsvConfigurationStepView view;
	protected EventBus publishBus;
	protected boolean headerRequired = false;
	
	@Inject
	public CsvConfigurationStepPresenter(@PublishBus EventBus publishBus) {
		super("csv-configuration", TrackerLabels.CSVCONFIG, "about this CSV…", "Adjust the parameters as needed.", PublishWizardStepButtons.BACKWARD, PublishWizardStepButtons.FORWARD);
		this.publishBus = publishBus;
		bind();
	}
	
	protected void bind()
	{
		publishBus.addHandler(ItemUpdatedEvent.getType(CsvConfiguration.class), new ItemUpdatedEvent.ItemUpdatedHandler<CsvConfiguration>() {

			@Override
			public void onItemUpdated(ItemUpdatedEvent<CsvConfiguration> event) {
				if (event.getSource() != CsvConfigurationStepPresenter.this) view.setCsvWriterConfiguration(event.getItem());
			}
		});
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		publishBus.fireEventFromSource(new ItemUpdatedEvent<CsvConfiguration>(view.getCsvWriterConfiguration()), this);
		return true;
	}

}
