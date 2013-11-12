package org.cotrix.web.publish.client.wizard;

import java.util.Arrays;
import java.util.List;

import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.step.DestinationNodeSelector;
import org.cotrix.web.publish.client.wizard.step.DetailsNodeSelector;
import org.cotrix.web.publish.client.wizard.step.TypeNodeSelector;
import org.cotrix.web.publish.client.wizard.step.codelistdetails.CodelistDetailsStepPresenterImpl;
import org.cotrix.web.publish.client.wizard.step.codelistselection.CodelistSelectionStepPresenter;
import org.cotrix.web.publish.client.wizard.step.csvconfiguration.CsvConfigurationStepPresenter;
import org.cotrix.web.publish.client.wizard.step.csvmapping.CsvMappingStepPresenter;
import org.cotrix.web.publish.client.wizard.step.destinationselection.DestinationSelectionStepPresenter;
import org.cotrix.web.publish.client.wizard.step.done.DoneStepPresenter;
import org.cotrix.web.publish.client.wizard.step.repositoryselection.RepositorySelectionStepPresenter;
import org.cotrix.web.publish.client.wizard.step.sdmxmapping.SdmxMappingStepPresenter;
import org.cotrix.web.publish.client.wizard.step.summary.SummaryStepPresenter;
import org.cotrix.web.publish.client.wizard.step.typeselection.TypeSelectionStepPresenter;
import org.cotrix.web.publish.client.wizard.task.RetrieveCSVConfigurationTask;
import org.cotrix.web.publish.client.wizard.task.RetrieveMappingsTask;
import org.cotrix.web.publish.client.wizard.task.RetrieveMetadataTask;
import org.cotrix.web.share.client.wizard.DefaultWizardActionHandler;
import org.cotrix.web.share.client.wizard.WizardAction;
import org.cotrix.web.share.client.wizard.WizardActionHandler;
import org.cotrix.web.share.client.wizard.WizardController;
import org.cotrix.web.share.client.wizard.flow.FlowManager;
import org.cotrix.web.share.client.wizard.flow.FlowManager.LabelProvider;
import org.cotrix.web.share.client.wizard.flow.builder.FlowManagerBuilder;
import org.cotrix.web.share.client.wizard.flow.builder.NodeBuilder.RootNodeBuilder;
import org.cotrix.web.share.client.wizard.flow.builder.NodeBuilder.SingleNodeBuilder;
import org.cotrix.web.share.client.wizard.flow.builder.NodeBuilder.SwitchNodeBuilder;
import org.cotrix.web.share.client.wizard.step.WizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PublishWizardPresenterImpl implements PublishWizardPresenter {

	public class PublishWizardActionHandler implements WizardActionHandler {

		@Override
		public boolean handle(WizardAction action, WizardController controller) {
			if (action instanceof PublishWizardAction) {
				PublishWizardAction importWizardAction = (PublishWizardAction)action;
				switch (importWizardAction) {
					
				}
			}
			return false;
		}
		
	}
	
	protected WizardController wizardController;
	
	protected FlowManager<WizardStep> flow;

	protected PublishWizardView view;

	protected EventBus publishEventBus;

	@Inject
	public PublishWizardPresenterImpl(@PublishBus EventBus publishEventBus, PublishWizardView view,
			CodelistSelectionStepPresenter codelistSelectionStep,
			
			DetailsNodeSelector detailsNodeSelector,
			RetrieveMetadataTask retrieveMetadataTask,
			CodelistDetailsStepPresenterImpl codelistDetailsStep,
			
			DestinationSelectionStepPresenter destinationSelectionStep,
			DestinationNodeSelector destinationSelector, 
			RepositorySelectionStepPresenter repositorySelectionStep,
			
			TypeSelectionStepPresenter typeSelectionStep,
			RetrieveCSVConfigurationTask retrieveCSVConfigurationTask,
			CsvConfigurationStepPresenter csvConfigurationStep,
			RetrieveMappingsTask retrieveMappingsTask,
			CsvMappingStepPresenter csvMappingStep,
			
			SdmxMappingStepPresenter sdmxMappingStep,

			SummaryStepPresenter summaryStep,
			DoneStepPresenter doneStep
			) {

		this.publishEventBus = publishEventBus;
		this.view = view;

		System.out.println("retrieveMetadataTask "+retrieveMetadataTask);
		
		
		RootNodeBuilder<WizardStep> root = FlowManagerBuilder.<WizardStep>startFlow(codelistSelectionStep);
		SwitchNodeBuilder<WizardStep> selectionStep = root.hasAlternatives(detailsNodeSelector);
		selectionStep.alternative(retrieveMetadataTask).next(codelistDetailsStep);
		
		SwitchNodeBuilder<WizardStep> destination = selectionStep.alternative(destinationSelectionStep).hasAlternatives(destinationSelector);
		
		TypeNodeSelector fileTypeSelector = new TypeNodeSelector(publishEventBus, retrieveCSVConfigurationTask, sdmxMappingStep);
		SwitchNodeBuilder<WizardStep> type = destination.alternative(typeSelectionStep).hasAlternatives(fileTypeSelector);
		SingleNodeBuilder<WizardStep> csvMapping = type.alternative(retrieveCSVConfigurationTask).next(csvConfigurationStep).next(retrieveMappingsTask).next(csvMappingStep);
		SingleNodeBuilder<WizardStep> sdmxMapping = type.alternative(sdmxMappingStep);
		
		TypeNodeSelector repositoryTypeSelector = new TypeNodeSelector(publishEventBus, csvMappingStep, sdmxMappingStep);
		SwitchNodeBuilder<WizardStep> repository = destination.alternative(repositorySelectionStep).hasAlternatives(repositoryTypeSelector);
		repository.alternative(csvMapping);
		repository.alternative(sdmxMapping);
		
		SingleNodeBuilder<WizardStep> summary = csvMapping.next(summaryStep);
		sdmxMapping.next(summary);
		
		summary.next(doneStep);
		

		/*SwitchNodeBuilder<WizardStep> upload = source.alternative(uploadStep).hasAlternatives(new TypeNodeSelector(importEventBus, csvPreviewStep, sdmxMappingStep));
		SingleNodeBuilder<WizardStep> csvPreview = upload.alternative(csvPreviewStep);
		SingleNodeBuilder<WizardStep> csvMapping = csvPreview.next(csvMappingStep);
		SingleNodeBuilder<WizardStep> sdmxMapping = upload.alternative(sdmxMappingStep);*/

	/*	SwitchNodeBuilder<WizardStep> selection = *//*.hasAlternatives(detailsNodeSelector);
		/*SingleNodeBuilder<WizardStep> codelistDetails = selection.alternative(codelistDetailsStep);
		SingleNodeBuilder<WizardStep> repositoryDetails = selection.alternative(repositoryDetailsStep);
		codelistDetails.next(repositoryDetails);
		
		SwitchNodeBuilder<WizardStep> retrieveAsset = selection.alternative(retrieveAssetTask).hasAlternatives(mappingNodeSelector);
		retrieveAsset.alternative(sdmxMapping);
		retrieveAsset.alternative(csvMapping);
		
		SingleNodeBuilder<WizardStep> summary = csvMapping.next(summaryStep);
		sdmxMapping.next(summary);

		summary.next(importTask).next(doneStep);*/

		flow = root.build();

		//only for debug
		if (Log.isTraceEnabled()) {
			String dot = flow.toDot(new LabelProvider<WizardStep>() {

				@Override
				public String getLabel(WizardStep item) {
					return item.getId();
				}
			});
			Log.trace("dot: "+dot);
		}
		
		List<WizardStep> visualSteps = Arrays.<WizardStep>asList(
				codelistSelectionStep, codelistDetailsStep, destinationSelectionStep, repositorySelectionStep, typeSelectionStep, csvConfigurationStep, 
				sdmxMappingStep, csvMappingStep, summaryStep, doneStep);

		wizardController = new WizardController(visualSteps, flow, view, publishEventBus);
		wizardController.addActionHandler(new DefaultWizardActionHandler());
		wizardController.addActionHandler(new PublishWizardActionHandler());
	}


	public void go(HasWidgets container) {
		container.add(view.asWidget());
		wizardController.init();
	}
}
