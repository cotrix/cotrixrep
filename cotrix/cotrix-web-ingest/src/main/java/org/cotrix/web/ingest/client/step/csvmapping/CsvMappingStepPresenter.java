package org.cotrix.web.ingest.client.step.csvmapping;

import java.util.List;

import org.cotrix.web.ingest.client.event.CodelistInfosLoadedEvent;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.event.MappingLoadedEvent;
import org.cotrix.web.ingest.client.event.MappingLoadingEvent;
import org.cotrix.web.ingest.client.event.MappingsUpdatedEvent;
import org.cotrix.web.ingest.client.event.MetadataUpdatedEvent;
import org.cotrix.web.ingest.client.step.AssetPreviewNodeSelector;
import org.cotrix.web.ingest.client.step.TrackerLabels;
import org.cotrix.web.ingest.client.wizard.ImportWizardStepButtons;
import org.cotrix.web.ingest.shared.AttributeMapping;
import org.cotrix.web.ingest.shared.AttributeType;
import org.cotrix.web.ingest.shared.CodelistInfo;
import org.cotrix.web.ingest.shared.ImportMetadata;
import org.cotrix.web.wizard.client.event.NavigationEvent;
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
public class CsvMappingStepPresenter extends AbstractVisualWizardStep implements VisualWizardStep, CsvMappingStepView.Presenter {

	static interface CsvMappingStepPresenterBinder extends EventBinder<CsvMappingStepPresenter> {}
	
	private CsvMappingStepView view;
	private EventBus importEventBus;
	private ImportMetadata metadata;
	private List<AttributeMapping> mappings;
	private List<CodelistInfo> codelistInfos;
	
	@Inject
	protected AssetPreviewNodeSelector previewNodeSelector;
	
	@Inject
	public CsvMappingStepPresenter(CsvMappingStepView view, @ImportBus EventBus importEventBus){
		super("csv-mapping", TrackerLabels.CUSTOMIZE, "Customize it", "Tell us what to import and how.", ImportWizardStepButtons.BACKWARD, ImportWizardStepButtons.FORWARD);
		this.view = view;
		view.setPresenter(this);
		//TODO
		view.setPreviewVisible(true);
		
		this.importEventBus = importEventBus;
	}
	
	@Inject
	void bind(CsvMappingStepPresenterBinder binder, @ImportBus EventBus importEventBus) {
		binder.bindEventHandlers(this, importEventBus);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}
	
	public boolean leave() {
		
		if (previewNodeSelector.toDetails()) return true;
		
		Log.trace("checking csv mapping");
		
		List<AttributeMapping> mappings = view.getMappings();
		Log.trace(mappings.size()+" mappings to check");
		
		boolean valid = validateMappings(mappings);
		
		String csvName = view.getCsvName();
		String version = view.getVersion();
		boolean sealed = view.getSealed();
		valid &= validateAttributes(csvName, version);
		
		if (valid) {
			importEventBus.fireEvent(new MappingsUpdatedEvent(mappings));
			
			ImportMetadata metadata = new ImportMetadata();
			metadata.setName(csvName);
			metadata.setVersion(version);
			metadata.setSealed(sealed);
			metadata.setAttributes(this.metadata.getAttributes());
			importEventBus.fireEvent(new MetadataUpdatedEvent(metadata, true));
		}
		
		return valid;
	}
	
	private boolean validateAttributes(String csvName, String version)
	{
		if (csvName==null || csvName.isEmpty()) {
			view.alert("You should choose a codelist name");
			return false;
		}
		
		if (version==null || version.isEmpty()) {
			view.alert("You should specify a codelist version");
			return false;
		}
		
		if (codelistInfos!=null && codelistInfos.contains(new CodelistInfo(csvName, version))) {
			view.alert("A codelist with name \""+csvName+"\" and version \""+version+"\" already exists");
			return false;
		}
		
		return true;
	}
	
	private boolean validateMappings(List<AttributeMapping> mappings)
	{
		
		//only one code
		int codeCount = 0;
		for (AttributeMapping mapping:mappings) {
			//Log.trace("checking mapping: "+mapping);
			if (!mapping.isMapped()) continue;
			
			if (mapping.getAttributeDefinition().getType()==AttributeType.CODE) codeCount++;
			
			if (mapping.getAttributeDefinition().getName().isEmpty()) {
				view.alert("don't leave columns blank, bin them instead");
				return false;
			}
			
			if (mapping.getAttributeDefinition().getType()==AttributeType.OTHER && 
					mapping.getAttributeDefinition().getCustomType().isEmpty()) {
				view.alert("don't leave the type blank");
				return false;
			}
		}
		
		if (codeCount==0) {
			view.alert("One column must contains codes.");
			return false;
		}
		
		if (codeCount>1) {
			view.alert("Only one column can contains codes.");
			return false;
		}
		
		return true;
	}
	
	@EventHandler
	void onMappingLoading(MappingLoadingEvent event) {
		view.setMappingLoading();
	}
	
	@EventHandler
	void onMappingLoaded(MappingLoadedEvent event) {
		mappings = event.getMappings();
		Log.trace("mappings: "+mappings);
		view.setMapping(mappings);
		view.unsetMappingLoading();
	}

	@EventHandler
	void onMetadataUpdated(MetadataUpdatedEvent event) {
		if (!event.isUserEdited()) {
			this.metadata = event.getMetadata();
			view.setCsvName(metadata.getName());
			view.setVersion(metadata.getVersion());
			view.setSealed(metadata.isSealed());
		}
	}
	
	@EventHandler
	void onCodelistInfosLoadedEvent(CodelistInfosLoadedEvent event) {
		this.codelistInfos = event.getCodelistInfos();
	}

	@Override
	public void onReload() {
		Log.trace("old mappings: "+mappings);
		view.setCsvName(metadata.getName());
		view.setVersion(metadata.getVersion());
		view.setSealed(metadata.isSealed());
		view.setMapping(mappings);
	}

	@Override
	public void onPreview() {
		previewNodeSelector.switchToPreview();
		importEventBus.fireEvent(NavigationEvent.FORWARD);
	}
}
