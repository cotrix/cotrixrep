package org.cotrix.web.publish.client.wizard.step.csvmapping;

import java.util.List;

import org.cotrix.web.publish.client.event.ItemUpdatedEvent;
import org.cotrix.web.publish.client.event.MappingsUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.publish.shared.Column;
import org.cotrix.web.publish.shared.Destination;
import org.cotrix.web.publish.shared.Format;
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
public class CsvMappingStepPresenterImpl extends AbstractVisualWizardStep implements CsvMappingStepPresenter {

	protected CsvMappingStepView view;
	protected EventBus publishBus;
	protected PublishMetadata metadata;
	protected List<AttributeMapping> mappings;
	protected Format formatType;
	protected boolean showMetadata = false;

	@Inject
	public CsvMappingStepPresenterImpl(CsvMappingStepView view, @PublishBus EventBus publishBus){
		super("csv-mapping", TrackerLabels.CUSTOMIZE, "Customize it", "Tells us what to include and how.", PublishWizardStepButtons.BACKWARD, PublishWizardStepButtons.FORWARD);
		this.view = view;
		view.setPresenter(this);

		this.publishBus = publishBus;

		bind();
	}

	protected void bind() {
		publishBus.addHandler(ItemUpdatedEvent.getType(Destination.class), new ItemUpdatedEvent.ItemUpdatedHandler<Destination>() {

			@Override
			public void onItemUpdated(ItemUpdatedEvent<Destination> event) {
				showMetadata = event.getItem() == Destination.CHANNEL;
				view.showMetadata(showMetadata);
			}
		});
		
		publishBus.addHandler(ItemUpdatedEvent.getType(Format.class), new ItemUpdatedEvent.ItemUpdatedHandler<Format>() {

			@Override
			public void onItemUpdated(ItemUpdatedEvent<Format> event) {
				formatType = event.getItem();		
			}
		});
		
		publishBus.addHandler(ItemUpdatedEvent.getType(PublishMetadata.class), new ItemUpdatedEvent.ItemUpdatedHandler<PublishMetadata>() {

			@Override
			public void onItemUpdated(ItemUpdatedEvent<PublishMetadata> event) {
				setMetadata(event.getItem());
			}
		});
		
		publishBus.addHandler(MappingsUpdatedEvent.TYPE, new MappingsUpdatedEvent.MappingsUpdatedHandler() {

			@Override
			public void onMappingUpdated(MappingsUpdatedEvent event) {
				if (event.getSource()!=CsvMappingStepPresenterImpl.this && formatType == Format.CSV) {
					mappings = event.getMappings();
					view.setMappings(mappings);
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

		if (showMetadata) {
			String csvName = view.getCsvName();
			String version = view.getVersion();
			valid &= validateAttributes(csvName, version);
		}

		if (valid) {
			publishBus.fireEventFromSource(new MappingsUpdatedEvent(mappings), this);

			PublishMetadata metadata = new PublishMetadata();
			metadata.setName(view.getCsvName());
			metadata.setVersion(view.getVersion());
			metadata.setSealed(view.getSealed());
			metadata.setAttributes(this.metadata.getAttributes());
			publishBus.fireEventFromSource(new ItemUpdatedEvent<PublishMetadata>(metadata), this);
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
			Column column = (Column) mapping.getMapping();
			if (mapping.isMapped() && column.getName().isEmpty()) {
				view.alert("don't leave columns blank, bin them instead");
				return false;
			}
		}

		return true;
	}

	protected void setMetadata(PublishMetadata metadata) {
		this.metadata = metadata;
		String name = metadata.getName();
		view.setCsvName(name == null?"":name);
		view.setVersion(metadata.getVersion());
		view.setSealed(metadata.isSealed());
	}

	@Override
	public void onReload() {
		view.setCsvName(metadata.getName());
		view.setVersion(metadata.getVersion());
		view.setSealed(metadata.isSealed());
		view.setMappings(mappings);
	}
}
