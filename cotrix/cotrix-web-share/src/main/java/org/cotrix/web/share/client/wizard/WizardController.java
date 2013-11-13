package org.cotrix.web.share.client.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cotrix.web.share.client.wizard.progresstracker.ProgressTracker.ProgressStep;
import org.cotrix.web.share.client.wizard.event.NavigationEvent;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent;
import org.cotrix.web.share.client.wizard.step.StepButton;
import org.cotrix.web.share.client.wizard.step.TaskWizardStep;
import org.cotrix.web.share.client.wizard.step.VisualStepConfiguration;
import org.cotrix.web.share.client.wizard.step.VisualWizardStep;
import org.cotrix.web.share.client.wizard.step.WizardStep;
import org.cotrix.web.share.client.wizard.step.WizardSteps;
import org.cotrix.web.share.client.wizard.WizardView.WizardButton;
import org.cotrix.web.share.client.wizard.flow.FlowManager;
import org.cotrix.web.share.client.wizard.flow.FlowUpdatedEvent;
import org.cotrix.web.share.client.wizard.flow.FlowUpdatedEvent.FlowUpdatedHandler;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.LegacyHandlerWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class WizardController implements WizardView.Presenter, HasValueChangeHandlers<WizardStep> {

	protected FlowManager<WizardStep> flow;

	protected WizardView view;

	protected EventBus eventBus;

	protected List<WizardActionHandler> handlers = new ArrayList<WizardActionHandler>();

	protected Map<WizardButton, WizardAction> buttonsActions = new HashMap<WizardButton, WizardAction>();
	protected VisualWizardStep currentVisualStep;

	public WizardController(List<WizardStep> steps, FlowManager<WizardStep> flow, WizardView view, EventBus eventBus) {

		this.flow = flow;
		this.view = view;
		this.eventBus = eventBus;

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
		for (WizardStep step:steps) registerStep(step);
		Log.trace("done");

		view.setPresenter(this);

		bind();
	}

	protected void registerStep(WizardStep step) {
		if (step instanceof VisualWizardStep) view.addStep((VisualWizardStep)step);
	}

	protected void bind()
	{
		eventBus.addHandler(NavigationEvent.TYPE, new NavigationEvent.NavigationHandler() {

			@Override
			public void onNavigation(NavigationEvent event) {
				Log.trace("onNavigation "+event.getNavigationType());
				switch (event.getNavigationType()) {
					case BACKWARD: goBack(); break;
					case FORWARD: goForward(); break;
				}
			}
		});
		eventBus.addHandler(ResetWizardEvent.TYPE, new ResetWizardEvent.ResetWizardHandler() {

			@Override
			public void onResetWizard(ResetWizardEvent event) {
				resetWizard();
			}
		});

		flow.addFlowUpdatedHandler(new FlowUpdatedHandler() {

			@Override
			public void onFlowUpdated(FlowUpdatedEvent event) {
				updateTrackerLabels();
			}
		});
	}

	public void addActionHandler(WizardActionHandler handler)
	{
		handlers.add(handler);
	}

	public void resetWizard() {
		flow.reset();
		currentVisualStep = null;
		buttonsActions.clear();
		updateTrackerLabels();
		updateCurrentStep();
	}

	public void init()
	{
		Log.trace("Initializing wizard");
		updateTrackerLabels();
		updateCurrentStep();
	}

	protected void updateTrackerLabels()
	{
		List<WizardStep> steps = flow.getCurrentFlow();
		Log.trace("New FLOW: "+WizardSteps.toString(steps));

		List<ProgressStep> psteps = new ArrayList<ProgressStep>();
		Set<String> saw = new HashSet<String>();
		for (WizardStep step:steps) {
			if (step instanceof VisualWizardStep) {
				ProgressStep pstep = ((VisualWizardStep)step).getConfiguration().getLabel();
				if (saw.contains(pstep.getId())) continue;
				psteps.add(pstep);
				saw.add(pstep.getId());
			}
		}
		Log.trace("Progress steps: "+psteps);

		view.setLabels(psteps);

		if (currentVisualStep!=null) view.showLabel(currentVisualStep.getConfiguration().getLabel());
	}

	protected void updateCurrentStep()
	{
		WizardStep currentStep = flow.getCurrentItem();
		Log.trace("current step "+currentStep.getId());
		if (currentStep instanceof VisualWizardStep) showStep((VisualWizardStep)currentStep);
		if (currentStep instanceof TaskWizardStep) runStep((TaskWizardStep)currentStep);
		ValueChangeEvent.fire(this, currentStep);
	}

	protected void showStep(VisualWizardStep step)
	{
		currentVisualStep = step;
		view.showStep(step);
		view.showLabel(step.getConfiguration().getLabel());
		VisualStepConfiguration configuration = step.getConfiguration();
		applyStepConfiguration(configuration);
	}

	protected void runStep(final TaskWizardStep step) {

		if (step.isComplete()) doAction(step.getAction());
		else {

			showProgress();
			step.run(new AsyncCallback<WizardAction>() {

				@Override
				public void onSuccess(WizardAction result) {
					doAction(result);
					hideProgress();
				}

				@Override
				public void onFailure(Throwable caught) {
					Log.trace("TaskWizardStep "+step.getId()+" failed", caught);
					hideProgress();
				}
			});
		}
	}

	protected void showProgress()
	{
		view.showProgress();
	}

	protected void hideProgress()
	{
		view.hideProgress();
	}

	protected void applyStepConfiguration(VisualStepConfiguration configuration)
	{
		String title = configuration.getTitle();
		view.setStepTitle(title);
		view.setStepSubtitle(configuration.getSubtitle());

		configureButtons(configuration.getButtons());
	}

	protected void configureButtons(StepButton ... buttons)
	{
		view.hideAllButtons();
		buttonsActions.clear();

		if (buttons!=null) for (StepButton button:buttons) configureButton(button);

	}

	protected void configureButton(StepButton button)
	{
		WizardButton wizardButton = button.getButton();
		view.showButton(wizardButton);
		buttonsActions.put(wizardButton, button.getAction());
	}

	protected void goForward()
	{
		boolean isComplete = flow.getCurrentItem().leave();
		if (!isComplete) return;

		if (flow.isLast())
			throw new IllegalStateException("There are no more steps");

		flow.goNext();
		updateCurrentStep();
	}

	protected void goBack()
	{
		if (flow.isFirst())
			throw new IllegalStateException("We are already in the first step");

		goBackToFirstVisual();
		updateCurrentStep();
	}

	protected void goBackToFirstVisual()
	{
		do {
			flow.goBack();
		} while (!flow.isFirst() && !(flow.getCurrentItem() instanceof VisualWizardStep));
	}

	protected void doAction(WizardAction action)
	{
		for (WizardActionHandler handler:handlers) {
			boolean handled = handler.handle(action, this);
			if (handled) return;
		}
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);		
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<WizardStep> handler) {
		return new LegacyHandlerWrapper(eventBus.addHandler(ValueChangeEvent.getType(), handler));
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
