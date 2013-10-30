package org.cotrix.web.publish.client.wizard;

import java.util.Arrays;
import java.util.List;

import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.step.DestinationNodeSelector;
import org.cotrix.web.publish.client.wizard.step.codelistselection.CodelistSelectionStepPresenter;
import org.cotrix.web.publish.client.wizard.step.destinationselection.DestinationSelectionStepPresenter;
import org.cotrix.web.publish.client.wizard.step.typeselection.TypeSelectionStepPresenter;
import org.cotrix.web.share.client.wizard.DefaultWizardActionHandler;
import org.cotrix.web.share.client.wizard.WizardAction;
import org.cotrix.web.share.client.wizard.WizardActionHandler;
import org.cotrix.web.share.client.wizard.WizardController;
import org.cotrix.web.share.client.wizard.flow.FlowManager;
import org.cotrix.web.share.client.wizard.flow.builder.FlowManagerBuilder;
import org.cotrix.web.share.client.wizard.flow.builder.NodeBuilder.RootNodeBuilder;
import org.cotrix.web.share.client.wizard.flow.builder.NodeBuilder.SingleNodeBuilder;
import org.cotrix.web.share.client.wizard.flow.builder.NodeBuilder.SwitchNodeBuilder;
import org.cotrix.web.share.client.wizard.step.WizardStep;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PublishWizardPresenterImpl implements PublishWizardPresenter {

	public class ImportWizardActionHandler implements WizardActionHandler {

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
			DestinationSelectionStepPresenter destinationSelectionStep,
			DestinationNodeSelector destinationSelector, 
			TypeSelectionStepPresenter typeSelectionStep
			) {

		this.publishEventBus = publishEventBus;
		this.view = view;

		System.out.println("codelistSelectionStep "+codelistSelectionStep);
		
		RootNodeBuilder<WizardStep> root = FlowManagerBuilder.<WizardStep>startFlow(codelistSelectionStep);
		SingleNodeBuilder<WizardStep> destination = root.next(destinationSelectionStep);
		SwitchNodeBuilder<WizardStep> source = destination.hasAlternatives(destinationSelector);

		/*SwitchNodeBuilder<WizardStep> upload = source.alternative(uploadStep).hasAlternatives(new TypeNodeSelector(importEventBus, csvPreviewStep, sdmxMappingStep));
		SingleNodeBuilder<WizardStep> csvPreview = upload.alternative(csvPreviewStep);
		SingleNodeBuilder<WizardStep> csvMapping = csvPreview.next(csvMappingStep);
		SingleNodeBuilder<WizardStep> sdmxMapping = upload.alternative(sdmxMappingStep);*/

	/*	SwitchNodeBuilder<WizardStep> selection = */source.alternative(typeSelectionStep);/*.hasAlternatives(detailsNodeSelector);
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
		/*if (Log.isTraceEnabled()) {
			String dot = flow.toDot(new LabelProvider<WizardStep>() {

				@Override
				public String getLabel(WizardStep item) {
					return item.getId();
				}
			});
			Log.trace("dot: "+dot);
		}*/
		
		List<WizardStep> steps = Arrays.<WizardStep>asList(codelistSelectionStep, destinationSelectionStep, typeSelectionStep);

		wizardController = new WizardController(steps, flow, view, publishEventBus);
		wizardController.addActionHandler(new DefaultWizardActionHandler());
		wizardController.addActionHandler(new ImportWizardActionHandler());
	}


	public void go(HasWidgets container) {
		container.add(view.asWidget());
		wizardController.init();
	}
}
