package org.cotrix.web.importwizard.client;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cotrix.web.importwizard.client.ImportWizardView.WizardButton;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.NewImportEvent;
import org.cotrix.web.importwizard.client.event.ResetWizardEvent;
import org.cotrix.web.importwizard.client.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.importwizard.client.flow.FlowManager;
import org.cotrix.web.importwizard.client.flow.FlowUpdatedEvent;
import org.cotrix.web.importwizard.client.flow.FlowUpdatedEvent.FlowUpdatedHandler;
import org.cotrix.web.importwizard.client.flow.builder.FlowManagerBuilder;
import org.cotrix.web.importwizard.client.flow.builder.NodeBuilder.RootNodeBuilder;
import org.cotrix.web.importwizard.client.flow.builder.NodeBuilder.SingleNodeBuilder;
import org.cotrix.web.importwizard.client.flow.builder.NodeBuilder.SwitchNodeBuilder;
import org.cotrix.web.importwizard.client.progresstracker.ProgressTracker.ProgressStep;
import org.cotrix.web.importwizard.client.step.VisualWizardStep;
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
import org.cotrix.web.importwizard.client.wizard.WizardAction;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.client.wizard.WizardStepConfiguration;
import org.cotrix.web.importwizard.client.wizard.event.NavigationEvent;
import org.cotrix.web.importwizard.client.wizard.event.NavigationEvent.NavigationHandler;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.LegacyHandlerWrapper;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImportWizardPresenterImpl implements ImportWizardPresenter, NavigationHandler, FlowUpdatedHandler, ResetWizardHandler, HasValueChangeHandlers<VisualWizardStep> {

	protected FlowManager<VisualWizardStep> flow;

	protected ImportWizardView view;

	protected EventBus importEventBus;
	
	protected EnumMap<WizardButton, WizardAction> buttonsActions = new EnumMap<WizardButton, WizardAction>(WizardButton.class);
	
	protected WizardAction backwardAction;
	protected WizardAction forwardAction;

	@Inject
	public ImportWizardPresenterImpl(@ImportBus final EventBus importEventBus, ImportWizardView view,  
			SourceSelectionStepPresenter sourceStep,
			UploadStepPresenter uploadStep,
			CsvPreviewStepPresenter csvPreviewStep,

			DetailsNodeSelector detailsNodeSelector,
			SelectionStepPresenter selectionStep,
			CodelistDetailsStepPresenter codelistDetailsStep,
			RepositoryDetailsStepPresenter repositoryDetailsStep,

			CsvMappingStepPresenter csvMappingStep,
			SdmxMappingStepPresenter sdmxMappingStep, 

			SummaryStepPresenter summaryStep,
			DoneStepPresenter doneStep,
			SourceNodeSelector selector,
			SaveCheckPoint saveCheckPoint) {

		this.importEventBus = importEventBus;
		this.view = view;
		this.view.setPresenter(this);

		RootNodeBuilder<VisualWizardStep> root = FlowManagerBuilder.<VisualWizardStep>startFlow(sourceStep);
		SwitchNodeBuilder<VisualWizardStep> source = root.hasAlternatives(selector);

		SwitchNodeBuilder<VisualWizardStep> upload = source.alternative(uploadStep).hasAlternatives(new TypeNodeSelector(importEventBus, csvPreviewStep, sdmxMappingStep));
		SingleNodeBuilder<VisualWizardStep> csvPreview = upload.alternative(csvPreviewStep);
		SingleNodeBuilder<VisualWizardStep> csvMapping = csvPreview.next(csvMappingStep);
		SingleNodeBuilder<VisualWizardStep> sdmxMapping = upload.alternative(sdmxMappingStep);

		SwitchNodeBuilder<VisualWizardStep> selection = source.alternative(selectionStep).hasAlternatives(detailsNodeSelector);
		SingleNodeBuilder<VisualWizardStep> codelistDetails = selection.alternative(codelistDetailsStep);
		SingleNodeBuilder<VisualWizardStep> repositoryDetails = selection.alternative(repositoryDetailsStep);
		codelistDetails.next(repositoryDetails);
		
		selection.alternative(sdmxMapping);
		selection.alternative(csvMapping);
		
		

		SingleNodeBuilder<VisualWizardStep> summary = csvMapping.next(summaryStep);
		sdmxMapping.next(summary);

		summary.hasCheckPoint(saveCheckPoint).next(doneStep);


		//.next(csvPreviewStep)
		//channel.next(csvPreview);

		//csvPreview.next(metadataStep).next(csvMappingStep).next(summaryStep).hasCheckPoint(saveCheckPoint).next(doneStep);

		flow = root.build();
		flow.addFlowUpdatedHandler(this);

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

		Log.trace("Adding steps");
		registerStep(sourceStep);
		registerStep(uploadStep);
		registerStep(selectionStep);
		registerStep(codelistDetailsStep);
		registerStep(repositoryDetailsStep);
		registerStep(csvPreviewStep);
		registerStep(csvMappingStep);
		registerStep(sdmxMappingStep);
		registerStep(summaryStep);
		registerStep(doneStep);
		Log.trace("done");

		bind();
	}

	@Override
	public void onResetWizard(ResetWizardEvent event) {
		flow.reset();
		updateTrackerLabels();
		updateCurrentStep();
	}

	protected void registerStep(VisualWizardStep step){
		view.addStep(step);
	}

	public void bind()
	{
		importEventBus.addHandler(NavigationEvent.TYPE, this);
		importEventBus.addHandler(ResetWizardEvent.TYPE, this);
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
		init();
	}

	protected void init()
	{
		Log.trace("Initializing wizard");
		updateTrackerLabels();
		updateCurrentStep();
	}

	protected void updateTrackerLabels()
	{
		List<VisualWizardStep> steps = flow.getCurrentFlow();
		Log.trace("New FLOW: "+steps);
		
		List<ProgressStep> psteps = new ArrayList<ProgressStep>();
		Set<String> saw = new HashSet<String>();
		for (VisualWizardStep step:steps) {
			ProgressStep pstep = step.getConfiguration().getLabel();
			if (saw.contains(pstep.getId())) continue;
			psteps.add(pstep);
			saw.add(pstep.getId());
		}
		Log.trace("Progress steps: "+psteps);
		
		view.setLabels(psteps);
		view.showLabel(flow.getCurrentItem().getConfiguration().getLabel());
	}

	protected void updateCurrentStep()
	{
		VisualWizardStep currentStep = flow.getCurrentItem();
		Log.trace("current step "+currentStep.getId());
		view.showStep(currentStep);
		view.showLabel(currentStep.getConfiguration().getLabel());
		WizardStepConfiguration configuration = currentStep.getConfiguration();
		applyStepConfiguration(configuration);
		ValueChangeEvent.fire(this, currentStep);
	}

	protected void applyStepConfiguration(WizardStepConfiguration configuration)
	{
		String title = configuration.getTitle();
		view.setStepTitle(title);
		view.setStepSubtitle(configuration.getSubtitle());

		configureButtons(configuration.getButtons());
	}
	
	protected void configureButtons(NavigationButtonConfiguration ... buttons)
	{
		view.hideAllButtons();
		buttonsActions.clear();
		
		if (buttons!=null) for (NavigationButtonConfiguration button:buttons) configureButton(button);
		
	}

	protected void configureButton(NavigationButtonConfiguration button)
	{
		WizardButton wizardButton = button.getWizardButton();
		view.showButton(wizardButton);
		buttonsActions.put(wizardButton, button.getAction());
	}

	protected void goForward()
	{
		boolean isComplete = flow.getCurrentItem().leave();
		if (!isComplete) return;

		//if (currentStepIndex == steps.size()-1)
		if (flow.isLast())
			throw new IllegalStateException("There are no more steps");

		//currentStepIndex++;
		flow.goNext();
		updateCurrentStep();
	}

	protected void goBack()
	{
		//if (currentStepIndex == 0) 
		if (flow.isFirst())
			throw new IllegalStateException("We are already in the first step");

		//currentStepIndex--;
		flow.goBack();
		updateCurrentStep();
	}
	
	protected void doAction(WizardAction action)
	{
		switch (action) {
			case BACK: goBack(); break;
			case NEXT: goForward(); break;
			case MANAGE: {
				//TODO
			} break;
			case NEW_IMPORT: {
				importEventBus.fireEvent(new NewImportEvent());
			} break;
			default:
				break;
		}
	}

	/**
	 * @param event
	 */
	@Override
	public void onNavigation(NavigationEvent event) {
		Log.trace("onNavigation "+event.getNavigationType());
		switch (event.getNavigationType()) {
			case BACKWARD: goBack(); break;
			case FORWARD: goForward(); break;
		}		
	}

	@Override
	public void onFlowUpdated(FlowUpdatedEvent event) {
		updateTrackerLabels();
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		importEventBus.fireEvent(event);		
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<VisualWizardStep> handler) {
		return new LegacyHandlerWrapper(importEventBus.addHandler(ValueChangeEvent.getType(), handler));
	}

	@Override
	public void onButtonClicked(WizardButton button) {
		WizardAction action = buttonsActions.get(button);
		if (action == null) {
			Log.fatal("Action not found for clicked button "+button);
			throw new IllegalArgumentException("Action not found for clicked button "+button);
		}
		doAction(action);
	}
}
