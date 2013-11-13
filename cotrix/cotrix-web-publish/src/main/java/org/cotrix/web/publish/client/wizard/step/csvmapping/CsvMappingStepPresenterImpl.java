package org.cotrix.web.publish.client.wizard.step.csvmapping;

import java.util.List;

import org.cotrix.web.publish.client.event.MappingLoadedEvent;
import org.cotrix.web.publish.client.event.MappingLoadedEvent.MappingLoadedHandler;
import org.cotrix.web.publish.client.event.MappingLoadingEvent;
import org.cotrix.web.publish.client.event.MappingLoadingEvent.MappingLoadingHandler;
import org.cotrix.web.publish.client.event.MappingsUpdatedEvent;
import org.cotrix.web.publish.client.event.MetadataUpdatedEvent;
import org.cotrix.web.publish.client.event.MetadataUpdatedEvent.MetadataUpdatedHandler;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.publish.shared.PublishMetadata;
import org.cotrix.web.share.client.wizard.step.AbstractVisualWizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvMappingStepPresenterImpl extends AbstractVisualWizardStep implements CsvMappingStepPresenter, MetadataUpdatedHandler, MappingLoadingHandler, MappingLoadedHandler {

	protected CsvMappingStepView view;
	protected EventBus publishEventBus;
	protected PublishMetadata metadata;
	protected List<AttributeMapping> mappings;

	@Inject
	public CsvMappingStepPresenterImpl(CsvMappingStepView view, @PublishBus EventBus publishEventBus){
		super("csv-mapping", TrackerLabels.CUSTOMIZE, "Customize it", "Tell us what to import and how.", PublishWizardStepButtons.BACKWARD, PublishWizardStepButtons.FORWARD);
		this.view = view;
		view.setPresenter(this);

		this.publishEventBus = publishEventBus;
		publishEventBus.addHandler(MetadataUpdatedEvent.TYPE, this);
		publishEventBus.addHandler(MappingLoadingEvent.TYPE, this);
		publishEventBus.addHandler(MappingLoadedEvent.TYPE, this);

		bind();
	}

	protected void bind() {
		publishEventBus.addHandler(MappingsUpdatedEvent.TYPE, new MappingsUpdatedEvent.MappingsUpdatedHandler() {

			@Override
			public void onMappingUpdated(MappingsUpdatedEvent event) {
				if (event.getSource()!=CsvMappingStepPresenterImpl.this) {
					mappings = event.getMappings();
					view.setMapping(mappings);
				}
			}
		});
	}

	/** 
	 * {@inheritDoc}
	 */
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		Log.trace("checking csv mapping");

		List<AttributeMapping> mappings = view.getMappings();
		Log.trace(mappings.size()+" mappings to check");

		boolean valid = validateMappings(mappings);

		/* TODO
		String csvName = view.getCsvName();
		String version = view.getVersion();
		boolean sealed = view.getSealed();
		valid &= validateAttributes(csvName, version);*/

		if (valid) {
			publishEventBus.fireEventFromSource(new MappingsUpdatedEvent(mappings), this);

			/*ImportMetadata metadata = new ImportMetadata();
			metadata.setName(csvName);
			metadata.setVersion(version);
			metadata.setSealed(sealed);
			metadata.setAttributes(this.metadata.getAttributes());
			publishEventBus.fireEvent(new MetadataUpdatedEvent(metadata, true));*/
		}

		return valid;
	}

	protected boolean validateAttributes(String csvName, String version)
	{
		if (csvName==null || csvName.isEmpty()) {
			view.alert("You should choose a codelist name");
			return false;
		}

		if (version==null || version.isEmpty()) {
			view.alert("You should specify a codelist version");
			return false;
		}

		return true;
	}

	protected boolean validateMappings(List<AttributeMapping> mappings)
	{

		for (AttributeMapping mapping:mappings) {
			//Log.trace("checking mapping: "+mapping);
			if (mapping.isMapped() && mapping.getColumnName().isEmpty()) {
				view.alert("don't leave columns blank, bin them instead");
				return false;
			}
		}

		return true;
	}

	@Override
	public void onMappingLoading(MappingLoadingEvent event) {
		view.setMappingLoading();
	}

	@Override
	public void onMappingLoaded(MappingLoadedEvent event) {
		mappings = event.getMappings();
		view.setMapping(mappings);
		view.unsetMappingLoading();
	}

	@Override
	public void onMetadataUpdated(MetadataUpdatedEvent event) {
		if (!event.isUserEdited()) {
			this.metadata = event.getMetadata();
			view.setCsvName(metadata.getName());
			view.setVersion(metadata.getVersion());
			view.setSealed(metadata.isSealed());
		}
	}

	@Override
	public void onReload() {
		view.setCsvName(metadata.getName());
		view.setVersion(metadata.getVersion());
		view.setSealed(metadata.isSealed());
		view.setMapping(mappings);
	}
}
