package org.cotrix.web.importwizard.client;

import java.util.ArrayList;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.step.WizardStep;
import org.cotrix.web.importwizard.client.step.channel.ChannelStepPresenter;
import org.cotrix.web.importwizard.client.step.done.DoneStepPresenter;
import org.cotrix.web.importwizard.client.step.mapping.MappingStepPresenterImpl;
import org.cotrix.web.importwizard.client.step.metadata.MetadataStepPresenter;
import org.cotrix.web.importwizard.client.step.preview.PreviewStepPresenterImpl;
import org.cotrix.web.importwizard.client.step.source.SourceStepPresenter;
import org.cotrix.web.importwizard.client.step.summary.SummaryStepPresenter;
import org.cotrix.web.importwizard.client.step.upload.UploadStepPresenter;
import org.cotrix.web.importwizard.client.step.upload.UploadStepPresenterImpl;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.client.wizard.WizardStepConfiguration;
import org.cotrix.web.importwizard.client.wizard.event.NavigationEvent;
import org.cotrix.web.importwizard.client.wizard.event.NavigationEvent.HasNavigationHandlers;
import org.cotrix.web.importwizard.client.wizard.event.NavigationEvent.NavigationHandler;
import org.cotrix.web.share.shared.CotrixImportModelController;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.inject.Inject;

public class ImportWizardPresenterImpl implements ImportWizardPresenter, NavigationHandler {

	protected ArrayList<WizardStep> steps  = new ArrayList<WizardStep>();
	protected int currentStepIndex = 0;
	protected WizardStep currentStep;


	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final ImportWizardView view;
	private CotrixImportModelController model;

	private DoneStepPresenter doneFormPresenter;

	private UploadStepPresenterImpl uploadFormPresenter;

	@Inject
	public ImportWizardPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus, ImportWizardView view,CotrixImportModelController model, 
			SourceStepPresenter sourceStepPresenter,
			UploadStepPresenter uploadFormPresenter,
			ChannelStepPresenter channelStepPresenter,
			MetadataStepPresenter metadataStepPresenter,
			PreviewStepPresenterImpl headerSelectionStepPresenter,
			MappingStepPresenterImpl headerTypeStepPresenter,
			SummaryStepPresenter summaryStepPresenter,
			DoneStepPresenter doneStepPresenter) {

		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.model = model;
		this.view.setPresenter(this);

		Log.trace("Adding steps");
		addStep(sourceStepPresenter);
		//addStep(uploadFormPresenter);
		addStep(channelStepPresenter);
		addStep(metadataStepPresenter);
		addStep(headerSelectionStepPresenter);
		addStep(headerTypeStepPresenter);
		addStep(summaryStepPresenter);
		addStep(doneStepPresenter);
		Log.trace("done");
	}

	protected void addStep(WizardStep step){
		steps.add(step);
		if (step instanceof HasNavigationHandlers) {
			Log.trace("registering "+step.getConfiguration().getLabel()+" as Navigation");
			((HasNavigationHandlers)step).addNavigationHandler(this);
		}
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
		init();
	}

	protected void init()
	{
		Log.trace("Initializing wizard");
		view.addSteps(steps);
		currentStepIndex = 0;
		updateCurrentStep();
	}

	protected void updateCurrentStep()
	{
		Log.trace("updateCurrentStep currentStepIndex: "+currentStepIndex);
		currentStep = steps.get(currentStepIndex);
		view.showStep(currentStepIndex);
		WizardStepConfiguration configuration = currentStep.getConfiguration();
		applyStepConfiguration(configuration);

		//check steps bounds
		if (currentStepIndex == 0) view.hideBackwardButton();
		if (currentStepIndex == steps.size()-1) view.hideForwardButton();
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
		boolean isComplete = currentStep.isComplete();
		if (!isComplete) return;

		if (currentStepIndex == steps.size()-1) throw new IllegalStateException("There are only "+steps.size()+" steps");

		currentStepIndex++;
		updateCurrentStep();
	}

	protected void goBack()
	{
		if (currentStepIndex == 0) throw new IllegalStateException("We are already in the first step");

		currentStepIndex--;
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





	public void onUploadOtherButtonClicked() {
		uploadFormPresenter.reset();
		model = new CotrixImportModelController();
		//view.showPrevStep(1);
	}

	public void onManageCodelistButtonClicked() {
		Window.alert("Go to manage codelist");
	}

	public void uploadFileFinish(SubmitCompleteEvent event) {
		//view.showPrevStep(steps.size());
		doneFormPresenter.setDoneTitle("Successful upload file to server !!!");
		doneFormPresenter.setWarningMessage(event.getResults());
	}

	@Override
	public void addForm(HasWidgets container) {
		// TODO Auto-generated method stub

	}


}
