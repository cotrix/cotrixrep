package org.cotrix.web.publish.client.wizard.step.formatselection;

import java.util.List;

import org.cotrix.web.common.shared.Format;
import org.cotrix.web.publish.client.event.ItemSelectedEvent;
import org.cotrix.web.publish.client.event.ItemUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.publish.shared.UIRepository;
import org.cotrix.web.wizard.client.event.NavigationEvent;
import org.cotrix.web.wizard.client.step.AbstractVisualWizardStep;
import org.cotrix.web.wizard.client.step.VisualWizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Source selection step.
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class FormatSelectionStepPresenter extends AbstractVisualWizardStep implements VisualWizardStep, FormatSelectionStepView.Presenter {
	
	protected static final ItemUpdatedEvent<Format> SDMX_EVENT = new ItemUpdatedEvent<Format>(Format.SDMX);
	protected static final ItemUpdatedEvent<Format> CSV_EVENT = new ItemUpdatedEvent<Format>(Format.CSV);
	protected static final ItemUpdatedEvent<Format> COMET_EVENT = new ItemUpdatedEvent<Format>(Format.COMET);

	protected FormatSelectionStepView view;
	
	protected EventBus publishBus;

	@Inject
	public FormatSelectionStepPresenter(FormatSelectionStepView view, @PublishBus EventBus publishBus) {
		super("formatSelection", TrackerLabels.TARGET, "How do we publish it?", "Choose a format.", PublishWizardStepButtons.BACKWARD);
		this.view = view;
		this.view.setPresenter(this);
		this.publishBus = publishBus;
		
		bind();
	}
	
	private void bind() {
		publishBus.addHandler(ItemSelectedEvent.getType(UIRepository.class), new ItemSelectedEvent.ItemSelectedHandler<UIRepository>() {

			@Override
			public void onItemSelected(ItemSelectedEvent<UIRepository> event) {
				UIRepository repository = event.getItem();
				setFormats(repository.getAvailableFormats());
			}
		});
	}
	
	private void setFormats(List<Format> formats) {
		for (Format format:Format.values()) {
			setFormatVisible(format, formats.contains(format));
		}
	}
	
	private void setFormatVisible(Format format, boolean visible) {
		switch (format) {
			case COMET: view.setCometVisible(visible); break;
			case CSV: view.setCSVVisible(visible); break;
			case SDMX: view.setSDMXVisible(visible); break;		}
	}

	/** 
	 * {@inheritDoc}
	 */
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	/** 
	 * {@inheritDoc}
	 */
	public boolean leave() {
		return true;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onSDMXButtonClick() {
		Log.trace("onSDMXButtonClick");
		publishBus.fireEvent(SDMX_EVENT);
		publishBus.fireEvent(NavigationEvent.FORWARD);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onCSVButtonClick() {
		Log.trace("onCSVButtonClick");
		publishBus.fireEvent(CSV_EVENT);
		publishBus.fireEvent(NavigationEvent.FORWARD);
	}

	@Override
	public void onCometButtonClick() {
		Log.trace("onCometButtonClick");
		publishBus.fireEvent(COMET_EVENT);
		publishBus.fireEvent(NavigationEvent.FORWARD);
	}
}
