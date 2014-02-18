package org.cotrix.web.importwizard.client.wizard;

import java.util.Arrays;
import java.util.List;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.ManageEvent;
import org.cotrix.web.importwizard.client.event.NewImportEvent;
import org.cotrix.web.importwizard.client.step.DetailsNodeSelector;
import org.cotrix.web.importwizard.client.step.MappingNodeSelector;
import org.cotrix.web.importwizard.client.step.SourceNodeSelector;
import org.cotrix.web.importwizard.client.step.TypeNodeSelector;
import org.cotrix.web.importwizard.client.step.codelistdetails.CodelistDetailsStepPresenter;
import org.cotrix.web.importwizard.client.step.csvmapping.CsvMappingStepPresenter;
import org.cotrix.web.importwizard.client.step.csvpreview.CsvPreviewStepPresenter;
import org.cotrix.web.importwizard.client.step.done.DoneStepPresenter;
import org.cotrix.web.importwizard.client.step.repositorydetails.RepositoryDetailsStepPresenter;
import org.cotrix.web.importwizard.client.step.sdmxmapping.SdmxMappingStepPresenter;
import org.cotrix.web.importwizard.client.step.selection.SelectionStepPresenter;
import org.cotrix.web.importwizard.client.step.sourceselection.SourceSelectionStepPresenter;
import org.cotrix.web.importwizard.client.step.summary.SummaryStepPresenter;
import org.cotrix.web.importwizard.client.step.upload.UploadStepPresenter;
import org.cotrix.web.importwizard.client.task.ImportTask;
import org.cotrix.web.importwizard.client.task.MappingsLoadingTask;
import org.cotrix.web.importwizard.client.task.RetrieveAssetTask;
import org.cotrix.web.wizard.client.DefaultWizardActionHandler;
import org.cotrix.web.wizard.client.WizardAction;
import org.cotrix.web.wizard.client.WizardActionHandler;
import org.cotrix.web.wizard.client.WizardController;
import org.cotrix.web.wizard.client.flow.FlowManager;
import org.cotrix.web.wizard.client.flow.builder.FlowManagerBuilder;
import org.cotrix.web.wizard.client.flow.builder.NodeBuilder.RootNodeBuilder;
import org.cotrix.web.wizard.client.flow.builder.NodeBuilder.SingleNodeBuilder;
import org.cotrix.web.wizard.client.flow.builder.NodeBuilder.SwitchNodeBuilder;
import org.cotrix.web.wizard.client.step.WizardStep;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImportWizardPresenterImpl implements ImportWizardPresenter {

	public class ImportWizardActionHandler implements WizardActionHandler {

		@Override
		public boolean handle(WizardAction action, WizardController controller) {
			if (action instanceof ImportWizardAction) {
				ImportWizardAction importWizardAction = (ImportWizardAction)action;
				switch (importWizardAction) {
					case MANAGE: {
						importEventBus.fireEvent(new ManageEvent());
						return true;
					}
					case NEW_IMPORT: {
						importEventBus.fireEvent(new NewImportEvent());
						return true;
					}
				}
			}
			return false;
		}
		
	}
	
	protected WizardController wizardController;
	
	protected FlowManager<WizardStep> flow;

	protected ImportWizardView view;

	protected EventBus importEventBus;

	@Inject
	public ImportWizardPresenterImpl(@ImportBus final EventBus importEventBus, ImportWizardView view,  
			SourceSelectionStepPresenter sourceStep,
			UploadStepPresenter uploadStep,
			CsvPreviewStepPresenter csvPreviewStep,

			DetailsNodeSelector detailsNodeSelector,
			SelectionStepPresenter selectionStep,
			CodelistDetailsStepPresenter codelistDetailsStep,
			RepositoryDetailsStepPresenter repositoryDetailsStep,
			
			RetrieveAssetTask retrieveAssetTask,
			MappingNodeSelector mappingNodeSelector,

			MappingsLoadingTask mappingsLoadingTask,
			CsvMappingStepPresenter csvMappingStep,
			SdmxMappingStepPresenter sdmxMappingStep, 
			
			SummaryStepPresenter summaryStep,
			

			
			DoneStepPresenter doneStep,
			SourceNodeSelector selector,
			
			//FIXME have to stay here to register the handler later :(
			ImportTask importTask
			) {

		this.importEventBus = importEventBus;
		this.view = view;

		RootNodeBuilder<WizardStep> root = FlowManagerBuilder.<WizardStep>startFlow(sourceStep);
		SwitchNodeBuilder<WizardStep> source = root.hasAlternatives(selector);

		SwitchNodeBuilder<WizardStep> upload = source.alternative(uploadStep).hasAlternatives(new TypeNodeSelector(importEventBus, csvPreviewStep, mappingsLoadingTask));
		SingleNodeBuilder<WizardStep> csvPreview = upload.alternative(csvPreviewStep);
		SingleNodeBuilder<WizardStep> csvMapping = csvPreview.next(mappingsLoadingTask).next(csvMappingStep);
		SingleNodeBuilder<WizardStep> sdmxMapping = upload.alternative(mappingsLoadingTask).next(sdmxMappingStep);

		SwitchNodeBuilder<WizardStep> selection = source.alternative(selectionStep).hasAlternatives(detailsNodeSelector);
		SingleNodeBuilder<WizardStep> codelistDetails = selection.alternative(codelistDetailsStep);
		SingleNodeBuilder<WizardStep> repositoryDetails = selection.alternative(repositoryDetailsStep);
		codelistDetails.next(repositoryDetails);
		
		SwitchNodeBuilder<WizardStep> retrieveAsset = selection.alternative(retrieveAssetTask).next(mappingsLoadingTask).hasAlternatives(mappingNodeSelector);
		retrieveAsset.alternative(sdmxMapping);
		retrieveAsset.alternative(csvMapping);
		
		SingleNodeBuilder<WizardStep> summary = csvMapping.next(summaryStep);
		sdmxMapping.next(summary);

		summary.next(importTask).next(doneStep);

		flow = root.build();

		//only for debug
		/*if (Log.isTraceEnabled()) {
			String dot = flow.toDot(new LabelProvider<WizardStep>() {

				@Override
				public String getLabel(WizardStep item) {
					return item.getId();
				}
			});
			Log.trace("dot: "+dot);
		}*/
		
		List<WizardStep> steps = Arrays.<WizardStep>asList(sourceStep, uploadStep, csvPreviewStep, csvMappingStep, sdmxMappingStep, selectionStep, codelistDetailsStep, repositoryDetailsStep, summaryStep, doneStep);

		wizardController = new WizardController(steps, flow, view, importEventBus);
		wizardController.addActionHandler(new DefaultWizardActionHandler());
		wizardController.addActionHandler(new ImportWizardActionHandler());
	}


	public void go(HasWidgets container) {
		container.add(view.asWidget());
		wizardController.init();
	}
}
