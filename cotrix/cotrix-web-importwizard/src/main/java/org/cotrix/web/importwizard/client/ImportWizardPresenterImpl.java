package org.cotrix.web.importwizard.client;

import java.util.List;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.ResetWizardEvent;
import org.cotrix.web.importwizard.client.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.importwizard.client.flow.FlowManager;
import org.cotrix.web.importwizard.client.flow.FlowUpdatedEvent;
import org.cotrix.web.importwizard.client.flow.FlowUpdatedEvent.FlowUpdatedHandler;
import org.cotrix.web.importwizard.client.flow.builder.FlowManagerBuilder;
import org.cotrix.web.importwizard.client.flow.builder.NodeBuilder.RootNodeBuilder;
import org.cotrix.web.importwizard.client.flow.builder.NodeBuilder.SingleNodeBuilder;
import org.cotrix.web.importwizard.client.step.WizardStep;
import org.cotrix.web.importwizard.client.step.channel.ChannelStepPresenter;
import org.cotrix.web.importwizard.client.step.csvmapping.CsvMappingStepPresenterImpl;
import org.cotrix.web.importwizard.client.step.done.DoneStepPresenter;
import org.cotrix.web.importwizard.client.step.metadata.MetadataStepPresenter;
import org.cotrix.web.importwizard.client.step.preview.CsvPreviewStepPresenterImpl;
import org.cotrix.web.importwizard.client.step.sourceselection.SourceSelectionStepPresenter;
import org.cotrix.web.importwizard.client.step.summary.SummaryStepPresenter;
import org.cotrix.web.importwizard.client.step.upload.UploadStepPresenter;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.client.wizard.WizardStepConfiguration;
import org.cotrix.web.importwizard.client.wizard.event.NavigationEvent;
import org.cotrix.web.importwizard.client.wizard.event.NavigationEvent.NavigationHandler;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImportWizardPresenterImpl implements ImportWizardPresenter, NavigationHandler, FlowUpdatedHandler, ResetWizardHandler {

	protected FlowManager<WizardStep> flow;

	protected ImportWizardView view;

	protected EventBus importEventBus;

	@Inject
	public ImportWizardPresenterImpl(@ImportBus final EventBus importEventBus, ImportWizardView view,  
			SourceSelectionStepPresenter sourceStepPresenter,
			UploadStepPresenter uploadFormPresenter,
			ChannelStepPresenter channelStepPresenter,
			
			MetadataStepPresenter metadataStepPresenter,
			CsvPreviewStepPresenterImpl previewStepPresenter,
			CsvMappingStepPresenterImpl headerTypeStepPresenter,
			SummaryStepPresenter summaryStepPresenter,
			DoneStepPresenter doneStepPresenter,
			SourceNodeSelector selector,
			SaveCheckPoint saveCheckPoint) {

		this.importEventBus = importEventBus;
		this.view = view;
		this.view.setPresenter(this);
		
		RootNodeBuilder<WizardStep> root = FlowManagerBuilder.<WizardStep>startFlow(sourceStepPresenter);
		
		SingleNodeBuilder<WizardStep> preview = root.next(uploadFormPresenter).next(previewStepPresenter);
		SingleNodeBuilder<WizardStep> channel = root.hasAlternatives(selector).alternative(channelStepPresenter);
		channel.next(preview);
		
		preview.next(metadataStepPresenter).next(headerTypeStepPresenter).next(summaryStepPresenter).hasCheckPoint(saveCheckPoint).next(doneStepPresenter);
		
		flow = root.build();
		flow.addFlowUpdatedHandler(this);

		Log.trace("Adding steps");
		registerStep(sourceStepPresenter);
		registerStep(uploadFormPresenter);
		registerStep(channelStepPresenter);
		registerStep(metadataStepPresenter);
		registerStep(previewStepPresenter);
		registerStep(headerTypeStepPresenter);
		registerStep(summaryStepPresenter);
		registerStep(doneStepPresenter);
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
		List<WizardStep> labels = flow.getCurrentFlow();
		Log.trace("FLOW:");
		for (WizardStep step:labels) Log.trace("step: "+step.getId());
		view.setLabels(labels);
		view.showLabel(flow.getCurrentItem());
	}

	protected void updateCurrentStep()
	{
		//Log.trace("updateCurrentStep currentStepIndex: "+currentStepIndex);
		WizardStep currentStep = flow.getCurrentItem(); //steps.get(currentStepIndex);
		Log.trace("current step "+currentStep.getId());
		view.showStep(currentStep);
		WizardStepConfiguration configuration = currentStep.getConfiguration();
		applyStepConfiguration(configuration);

		//check steps bounds
		if (flow.isFirst()) view.hideBackwardButton();
		if (flow.isLast()) view.hideForwardButton();
	}

	protected void applyStepConfiguration(WizardStepConfiguration configuration)
	{
		String title = configuration.getTitle();
		view.setStepTitle(title);

		configureBackwardButton(configuration.getBackwardButton());
		configureForwardButton(configuration.getForwardButton());
	}

	protected void configureBackwardButton(NavigationButtonConfiguration buttonConfiguration)
	{
		if (buttonConfiguration == NavigationButtonConfiguration.NONE) view.hideBackwardButton();
		else {
			String label = buttonConfiguration.getLabel();
			view.setBackwardButtonLabel(label);
			view.showBackwardButton();
		}
	}

	protected void configureForwardButton(NavigationButtonConfiguration buttonConfiguration)
	{
		if (buttonConfiguration == NavigationButtonConfiguration.NONE) view.hideForwardButton();
		else {
			String label = buttonConfiguration.getLabel();
			view.setForwardButtonLabel(label);
			view.showForwardButton();
		}
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

	/** 
	 * {@inheritDoc}
	 */
	public void onFowardButtonClicked() {
		goForward();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void onBackwardButtonClicked() {
		goBack();
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


}
