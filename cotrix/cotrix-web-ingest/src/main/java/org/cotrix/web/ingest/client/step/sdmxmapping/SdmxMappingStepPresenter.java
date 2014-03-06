package org.cotrix.web.ingest.client.step.sdmxmapping;

import java.util.List;

import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.event.MappingLoadedEvent;
import org.cotrix.web.ingest.client.event.MappingLoadingEvent;
import org.cotrix.web.ingest.client.event.MappingsUpdatedEvent;
import org.cotrix.web.ingest.client.event.MetadataUpdatedEvent;
import org.cotrix.web.ingest.client.step.TrackerLabels;
import org.cotrix.web.ingest.client.wizard.ImportWizardStepButtons;
import org.cotrix.web.ingest.shared.AttributeMapping;
import org.cotrix.web.ingest.shared.ImportMetadata;
import org.cotrix.web.wizard.client.step.AbstractVisualWizardStep;
import org.cotrix.web.wizard.client.step.VisualWizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class SdmxMappingStepPresenter extends AbstractVisualWizardStep implements VisualWizardStep, SdmxMappingStepView.Presenter {

	protected static interface SdmxMappingStepPresenterEventBinder extends EventBinder<SdmxMappingStepPresenter> {}

	@Inject @ImportBus
	protected EventBus importEventBus;
	protected SdmxMappingStepView view;
	protected ImportMetadata metadata;
	protected List<AttributeMapping> mappings;

	@Inject
	public SdmxMappingStepPresenter(SdmxMappingStepView view){
		super("sdmx-mapping", TrackerLabels.CUSTOMIZE, "Customize it", "Tell us what to import and how.", ImportWizardStepButtons.BACKWARD, ImportWizardStepButtons.FORWARD);
		this.view = view;
		this.view.setPresenter(this);
	}
	
	@Inject
	private void bind(SdmxMappingStepPresenterEventBinder binder, @ImportBus EventBus importEventBus) {
		binder.bindEventHandlers(this, importEventBus);
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
		String codelistName = view.getCodelistName();
		String version = view.getVersion();
		boolean sealed = view.getSealed();
		valid &= validateAttributes(codelistName, version);

		if (valid) {

			importEventBus.fireEvent(new MappingsUpdatedEvent(mappings));
			
			ImportMetadata metadata = new ImportMetadata();
			metadata.setName(codelistName);
			metadata.setVersion(version);
			metadata.setSealed(sealed);
			metadata.setAttributes(this.metadata.getAttributes());
			importEventBus.fireEvent(new MetadataUpdatedEvent(metadata, true));
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
			if (mapping.isMapped() && mapping.getAttributeDefinition().getName().isEmpty()) {
				view.alert("don't leave elements blank, bin them instead");
				return false;
			}
		}

		return true;
	}

	@EventHandler
	void onMetadataUpdated(MetadataUpdatedEvent event) {
		if (!event.isUserEdited()) {
			this.metadata = event.getMetadata();
			String name = metadata.getName();
			view.setCodelistName(name == null?"":name);
			view.setVersion(metadata.getVersion());
			view.setSealed(metadata.isSealed());
		}
	}
	
	@EventHandler
	void onMappingLoading(MappingLoadingEvent event) {
		view.setMappingLoading();
	}
	
	@EventHandler
	void onMappingLoaded(MappingLoadedEvent event) {
		mappings = event.getMappings();
		view.setMappings(mappings);
		view.unsetMappingLoading();
	}

	@Override
	public void onReload() {
		view.setCodelistName(metadata.getName());
		view.setVersion(metadata.getVersion());
		view.setSealed(metadata.isSealed());
		view.setMappings(mappings);
	}
}
