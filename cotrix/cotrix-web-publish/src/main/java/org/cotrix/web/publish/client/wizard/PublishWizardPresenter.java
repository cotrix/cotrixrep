package org.cotrix.web.publish.client.wizard;

import java.util.Arrays;
import java.util.List;

import org.cotrix.web.common.client.Presenter;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.step.DestinationNodeSelector;
import org.cotrix.web.publish.client.wizard.step.DetailsNodeSelector;
import org.cotrix.web.publish.client.wizard.step.RepositoryDetailsNodeSelector;
import org.cotrix.web.publish.client.wizard.step.SubTypeNodeSelector;
import org.cotrix.web.publish.client.wizard.step.TypeNodeSelector;
import org.cotrix.web.publish.client.wizard.step.codelistdetails.CodelistDetailsStepPresenter;
import org.cotrix.web.publish.client.wizard.step.codelistselection.CodelistSelectionStepPresenter;
import org.cotrix.web.publish.client.wizard.step.cometmapping.CometMappingStepPresenter;
import org.cotrix.web.publish.client.wizard.step.csvconfiguration.CsvConfigurationStepPresenter;
import org.cotrix.web.publish.client.wizard.step.csvmapping.CsvMappingStepPresenter;
import org.cotrix.web.publish.client.wizard.step.destinationselection.DestinationSelectionStepPresenter;
import org.cotrix.web.publish.client.wizard.step.done.DoneStepPresenter;
import org.cotrix.web.publish.client.wizard.step.formatselection.FormatSelectionStepPresenter;
import org.cotrix.web.publish.client.wizard.step.repositorydetails.RepositoryDetailsStepPresenter;
import org.cotrix.web.publish.client.wizard.step.repositoryselection.RepositorySelectionStepPresenter;
import org.cotrix.web.publish.client.wizard.step.sdmxmapping.SdmxMappingStepPresenter;
import org.cotrix.web.publish.client.wizard.step.summary.SummaryStepPresenter;
import org.cotrix.web.publish.client.wizard.step.typeselection.TypeSelectionStepPresenter;
import org.cotrix.web.publish.client.wizard.task.CleanMappingsTask;
import org.cotrix.web.publish.client.wizard.task.PublishTask;
import org.cotrix.web.publish.client.wizard.task.RetrieveCSVConfigurationTask;
import org.cotrix.web.publish.client.wizard.task.RetrieveMappingsTask;
import org.cotrix.web.publish.client.wizard.task.RetrieveMetadataTask;
import org.cotrix.web.publish.client.wizard.task.RetrieveRepositoryDetailsTask;
import org.cotrix.web.wizard.client.DefaultWizardActionHandler;
import org.cotrix.web.wizard.client.WizardController;
import org.cotrix.web.wizard.client.flow.FlowManager;
import org.cotrix.web.wizard.client.flow.builder.FlowManagerBuilder;
import org.cotrix.web.wizard.client.flow.builder.NodeBuilder.RootNodeBuilder;
import org.cotrix.web.wizard.client.flow.builder.NodeBuilder.SingleNodeBuilder;
import org.cotrix.web.wizard.client.flow.builder.NodeBuilder.SwitchNodeBuilder;
import org.cotrix.web.wizard.client.step.WizardStep;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class PublishWizardPresenter implements Presenter {
	
	protected WizardController wizardController;
	
	protected FlowManager<WizardStep> flow;

	protected PublishWizardView view;

	protected EventBus publishEventBus;

	@Inject
	public PublishWizardPresenter(@PublishBus EventBus publishEventBus, PublishWizardView view,
			CodelistSelectionStepPresenter codelistSelectionStep,
			
			DetailsNodeSelector detailsNodeSelector,
			RetrieveMetadataTask retrieveMetadataTask,
			CodelistDetailsStepPresenter codelistDetailsStep,
			
			DestinationSelectionStepPresenter destinationSelectionStep,
			RepositorySelectionStepPresenter repositorySelectionStep,
			RetrieveRepositoryDetailsTask repositoryDetailsTask,
			RepositoryDetailsStepPresenter repositoryDetailsStep,
			FormatSelectionStepPresenter formatSelectionStep,
			
			TypeSelectionStepPresenter fileTypeSelectionStep,
			RetrieveCSVConfigurationTask retrieveCSVConfigurationTask,
			CsvConfigurationStepPresenter csvConfigurationStep,
			RetrieveMappingsTask retrieveMappingsTask,
			CsvMappingStepPresenter csvMappingStep,
			CleanMappingsTask cleanMappingsTask,
			
			SdmxMappingStepPresenter sdmxMappingStep,
			CometMappingStepPresenter cometMappingStep,

			SummaryStepPresenter summaryStep,
			PublishTask publishTask,
			DoneStepPresenter doneStep,
			
			PublishWizardActionHandler wizardActionHandler
			) {

		this.publishEventBus = publishEventBus;
		this.view = view;

		System.out.println("retrieveMetadataTask "+retrieveMetadataTask);
	
		
		RootNodeBuilder<WizardStep> root = FlowManagerBuilder.<WizardStep>startFlow(codelistSelectionStep);
		
		SwitchNodeBuilder<WizardStep> selectionStep = root.hasAlternatives(detailsNodeSelector);
		
		//codelist details visualization
		selectionStep.alternative(retrieveMetadataTask).next(codelistDetailsStep);
		
		DestinationNodeSelector destinationSelector = new DestinationNodeSelector(publishEventBus, repositorySelectionStep, fileTypeSelectionStep);
		SwitchNodeBuilder<WizardStep> destination = selectionStep.alternative(destinationSelectionStep).hasAlternatives(destinationSelector);
		
		
		TypeNodeSelector fileTypeSelector = new TypeNodeSelector(publishEventBus, csvMappingStep, sdmxMappingStep, cometMappingStep);
		SingleNodeBuilder<WizardStep> typeSelection = destination.alternative(fileTypeSelectionStep);
		SwitchNodeBuilder<WizardStep> type = typeSelection.next(retrieveMappingsTask).hasAlternatives(fileTypeSelector);
			
		SingleNodeBuilder<WizardStep> csvMapping = type.alternative(csvMappingStep);
		SingleNodeBuilder<WizardStep> summary = csvMapping.next(summaryStep);
		type.alternative(sdmxMappingStep).next(summary);
		type.alternative(cometMappingStep).next(summary);
		
		RepositoryDetailsNodeSelector repositoryDetailsNodeSelector = new RepositoryDetailsNodeSelector(publishEventBus, repositoryDetailsTask, retrieveMappingsTask, formatSelectionStep);
		SwitchNodeBuilder<WizardStep> repository = destination.alternative(repositorySelectionStep).hasAlternatives(repositoryDetailsNodeSelector);
		repository.alternative(repositoryDetailsTask).next(repositoryDetailsStep);
		repository.alternative(retrieveMappingsTask).next(type);
		repository.alternative(formatSelectionStep).next(type);
	
		DestinationNodeSelector csvDestinationSelector = new DestinationNodeSelector(publishEventBus, summaryStep, retrieveCSVConfigurationTask);
		SwitchNodeBuilder<WizardStep> csvPostMapping = csvMapping.hasAlternatives(csvDestinationSelector); 
		csvPostMapping.alternative(retrieveCSVConfigurationTask).next(csvConfigurationStep).next(summary);
		csvPostMapping.alternative(summary);
		
		summary.next(publishTask).next(doneStep);

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
		
		List<WizardStep> visualSteps = Arrays.<WizardStep>asList(
				codelistSelectionStep, codelistDetailsStep, destinationSelectionStep, repositorySelectionStep, repositoryDetailsStep, formatSelectionStep, fileTypeSelectionStep, csvConfigurationStep, 
				sdmxMappingStep, csvMappingStep, cometMappingStep, summaryStep, doneStep);

		wizardController = new WizardController(visualSteps, flow, view, publishEventBus);
		wizardController.addActionHandler(new DefaultWizardActionHandler());
		wizardController.addActionHandler(wizardActionHandler);
	}


	public void go(HasWidgets container) {
		container.add(view.asWidget());
		wizardController.init();
	}
}
