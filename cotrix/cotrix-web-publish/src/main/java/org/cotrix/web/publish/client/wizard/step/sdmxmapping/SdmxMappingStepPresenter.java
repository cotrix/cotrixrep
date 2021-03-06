package org.cotrix.web.publish.client.wizard.step.sdmxmapping;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.client.widgets.dialog.AlertDialog;
import org.cotrix.web.common.shared.Format;
import org.cotrix.web.publish.client.event.ItemUpdatedEvent;
import org.cotrix.web.publish.client.event.MappingsUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.publish.shared.DefinitionsMappings;
import org.cotrix.web.publish.shared.Destination;
import org.cotrix.web.publish.shared.PublishMetadata;
import org.cotrix.web.wizard.client.step.AbstractVisualWizardStep;
import org.cotrix.web.wizard.client.step.VisualWizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class SdmxMappingStepPresenter extends AbstractVisualWizardStep implements VisualWizardStep, SdmxMappingStepView.Presenter {

	private SdmxMappingStepView view;
	private EventBus publishBus;
	private PublishMetadata metadata;
	private DefinitionsMappings mappings;
	private Format formatType;
	private boolean showMetadata = false;
	
	@Inject
	private AlertDialog alertDialog;

	@Inject
	public SdmxMappingStepPresenter(SdmxMappingStepView view, @PublishBus EventBus publishBus){
		super("sdmx-mapping", TrackerLabels.CUSTOMIZE, "Customize it", "Tell us what to import and how.", PublishWizardStepButtons.BACKWARD, PublishWizardStepButtons.FORWARD);
		this.view = view;
		this.view.setPresenter(this);

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
				if (event.getSource()!=SdmxMappingStepPresenter.this && formatType == Format.SDMX) {
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
		Log.trace("checking sdmx mapping");

		DefinitionsMappings mappings = view.getMappings();

		boolean valid = validateMappings(mappings);

		if (showMetadata) {
			String csvName = view.getCodelistName();
			String version = view.getVersion();
			valid &= validateAttributes(csvName, version);
		}

		if (valid) {
			Log.trace("sending mapping update "+mappings);
			publishBus.fireEventFromSource(new MappingsUpdatedEvent(mappings), this);

			PublishMetadata metadata = new PublishMetadata();
			metadata.setName(ValueUtils.getValue(view.getCodelistName()));
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
			alertDialog.center("You should choose a codelist name");
			return false;
		}

		if (version==null || version.isEmpty()) {
			alertDialog.center("You should specify a codelist version");
			return false;
		}

		return true;
	}

	protected boolean validateMappings(DefinitionsMappings mappings)
	{

		return true;
	}


	protected void setMetadata(PublishMetadata metadata) {
		this.metadata = metadata;
		view.setCodelistName(ValueUtils.getValue(metadata.getName()));
		view.setVersion(metadata.getVersion());
		view.setSealed(metadata.isSealed());
	}

	@Override
	public void onReload() {
		view.setCodelistName(ValueUtils.getValue(metadata.getName()));
		view.setVersion(metadata.getVersion());
		view.setSealed(metadata.isSealed());
		view.setMappings(mappings);
	}
}
