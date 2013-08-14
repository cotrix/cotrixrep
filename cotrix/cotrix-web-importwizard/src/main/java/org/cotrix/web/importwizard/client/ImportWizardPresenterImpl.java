package org.cotrix.web.importwizard.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.NewImportEvent;
import org.cotrix.web.importwizard.client.event.ResetWizardEvent;
import org.cotrix.web.importwizard.client.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.importwizard.client.flow.FlowManager;
import org.cotrix.web.importwizard.client.flow.FlowManager.LabelProvider;
import org.cotrix.web.importwizard.client.flow.FlowUpdatedEvent;
import org.cotrix.web.importwizard.client.flow.FlowUpdatedEvent.FlowUpdatedHandler;
import org.cotrix.web.importwizard.client.flow.builder.FlowManagerBuilder;
import org.cotrix.web.importwizard.client.flow.builder.NodeBuilder.RootNodeBuilder;
import org.cotrix.web.importwizard.client.flow.builder.NodeBuilder.SingleNodeBuilder;
import org.cotrix.web.importwizard.client.flow.builder.NodeBuilder.SwitchNodeBuilder;
import org.cotrix.web.importwizard.client.progresstracker.ProgressTracker.ProgressStep;
import org.cotrix.web.importwizard.client.step.WizardStep;
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
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.client.wizard.WizardStepConfiguration;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration.ButtonAction;
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
public class ImportWizardPresenterImpl implements ImportWizardPresenter, NavigationHandler, FlowUpdatedHandler, ResetWizardHandler, HasValueChangeHandlers<WizardStep> {

	protected FlowManager<WizardStep> flow;

	protected ImportWizardView view;

	protected EventBus importEventBus;
	
	protected ButtonAction backwardAction;
	protected ButtonAction forwardAction;

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

		RootNodeBuilder<WizardStep> root = FlowManagerBuilder.<WizardStep>startFlow(sourceStep);
		SwitchNodeBuilder<WizardStep> source = root.hasAlternatives(selector);

		SwitchNodeBuilder<WizardStep> upload = source.alternative(uploadStep).hasAlternatives(new TypeNodeSelector(importEventBus, csvPreviewStep, sdmxMappingStep));
		SingleNodeBuilder<WizardStep> csvPreview = upload.alternative(csvPreviewStep);
		SingleNodeBuilder<WizardStep> csvMapping = csvPreview.next(csvMappingStep);
		SingleNodeBuilder<WizardStep> sdmxMapping = upload.alternative(sdmxMappingStep);

		SwitchNodeBuilder<WizardStep> selection = source.alternative(selectionStep).hasAlternatives(detailsNodeSelector);
		SingleNodeBuilder<WizardStep> codelistDetails = selection.alternative(codelistDetailsStep);
		SingleNodeBuilder<WizardStep> repositoryDetails = selection.alternative(repositoryDetailsStep);
		codelistDetails.next(repositoryDetails);
		
		selection.alternative(sdmxMapping);
		selection.alternative(csvMapping);
		
		

		SingleNodeBuilder<WizardStep> summary = csvMapping.next(summaryStep);
		sdmxMapping.next(summary);

		summary.hasCheckPoint(saveCheckPoint).next(doneStep);


		//.next(csvPreviewStep)
		//channel.next(csvPreview);

		//csvPreview.next(metadataStep).next(csvMappingStep).next(summaryStep).hasCheckPoint(saveCheckPoint).next(doneStep);

		flow = root.build();
		flow.addFlowUpdatedHandler(this);

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

	protected void registerStep(WizardStep step){
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
		List<WizardStep> steps = flow.getCurrentFlow();
		Log.trace("New FLOW:");
		for (WizardStep step:steps) Log.trace("step: "+step.getId());
		
		List<ProgressStep> psteps = new ArrayList<ProgressStep>();
		Set<String> saw = new HashSet<String>();
		for (WizardStep step:steps) {
			ProgressStep pstep = step.getConfiguration().getLabel();
			if (saw.contains(pstep.getId())) continue;
			psteps.add(pstep);
		}
		
		view.setLabels(psteps);
		view.showLabel(flow.getCurrentItem().getConfiguration().getLabel());
	}

	protected void updateCurrentStep()
	{
		WizardStep currentStep = flow.getCurrentItem();
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

		configureBackwardButton(configuration.getBackwardButton());
		configureForwardButton(configuration.getForwardButton());
	}

	protected void configureBackwardButton(NavigationButtonConfiguration buttonConfiguration)
	{
		if (buttonConfiguration == NavigationButtonConfiguration.NONE) view.hideBackwardButton();
		else {
			String label = buttonConfiguration.getLabel();
			String style = buttonConfiguration.getStyle();
			view.setBackwardButton(label, style);
			view.showBackwardButton();
		}
		backwardAction = buttonConfiguration.getAction();
	}

	protected void configureForwardButton(NavigationButtonConfiguration buttonConfiguration)
	{
		if (buttonConfiguration == NavigationButtonConfiguration.NONE) view.hideForwardButton();
		else {
			String label = buttonConfiguration.getLabel();
			String style = buttonConfiguration.getStyle();
			view.setForwardButton(label, style);
			view.showForwardButton();
		}
		forwardAction = buttonConfiguration.getAction();
	}

	protected void goForward()
	{
		boolean isComplete = flow.getCurrentItem().isComplete();
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
	
	protected void doAction(ButtonAction action)
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
	 * {@inheritDoc}
	 */
	public void onFowardButtonClicked() {
		doAction(forwardAction);
	}

	/** 
	 * {@inheritDoc}
	 */
	public void onBackwardButtonClicked() {
		doAction(backwardAction);
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
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<WizardStep> handler) {
		return new LegacyHandlerWrapper(importEventBus.addHandler(ValueChangeEvent.getType(), handler));
	}


}
