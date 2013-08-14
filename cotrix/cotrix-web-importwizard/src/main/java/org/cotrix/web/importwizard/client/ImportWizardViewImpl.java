package org.cotrix.web.importwizard.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cotrix.web.importwizard.client.progresstracker.ProgressTracker;
import org.cotrix.web.importwizard.client.progresstracker.ProgressTracker.ProgressStep;
import org.cotrix.web.importwizard.client.step.WizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ImportWizardViewImpl extends Composite implements ImportWizardView {

	@UiTemplate("ImportWizard.ui.xml")
	interface ImportWizardUiBinder extends UiBinder<Widget, ImportWizardViewImpl> {}
	private static ImportWizardUiBinder uiBinder = GWT.create(ImportWizardUiBinder.class);

	@UiField FlowPanel progressTrackerPanel;

	//FIXME we should use DeckLayoutPanel
	@UiField DeckPanel stepsPanel;

	@UiField Label title;
	@UiField Label subtitle;

	@UiField Button forwardButton;
	@UiField Button backwardButton;

	private ProgressTracker progressTracker;
	protected int currentIndex = 0;
	protected Map<String, Integer> decksIndexes;
	protected Map<String, Integer> labelsIndexes;

	private Presenter presenter;
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public ImportWizardViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		progressTracker = new ProgressTracker();
		progressTrackerPanel.add(progressTracker);
		decksIndexes = new HashMap<String, Integer>();
	}

	public void addStep(WizardStep step)
	{
		Log.trace("Adding "+step.getId());
		step.go(stepsPanel);
		decksIndexes.put(step.getId(), currentIndex++);
		Log.trace("Totalpanels in deck "+stepsPanel.getWidgetCount());

	}

	public void setLabels(List<ProgressStep> steps) {
		Log.trace("setting "+steps.size()+" labels");
		progressTracker.init(steps);	
	}

	public void setStepTitle(String title)
	{
		this.title.setText(title);
	}

	public void setStepSubtitle(String subtitle)
	{
		this.subtitle.setText(subtitle);
	}

	public void showStep(WizardStep step)
	{
		Log.trace("showStep "+step.getId()+" steps: "+stepsPanel.getWidgetCount());
		Integer deckIndex = decksIndexes.get(step.getId());
		if (deckIndex == null) throw new IllegalArgumentException("Step "+step.getId()+" not found in deck, forgot to register?");
		stepsPanel.showWidget(deckIndex);
	}

	public void showLabel(ProgressStep step)
	{
		progressTracker.setCurrentStep(step);
	}

	public void hideBackwardButton()
	{
		backwardButton.setVisible(false);
	}

	public void showBackwardButton()
	{
		backwardButton.setVisible(true);
	}

	public void setBackwardButton(String label, String style)
	{
		backwardButton.setText(label);
		backwardButton.setStyleName(style);
	}

	public void hideForwardButton()
	{
		forwardButton.setVisible(false);
	}

	public void showForwardButton()
	{
		forwardButton.setVisible(true);
	}

	public void setForwardButton(String label, String style)
	{
		forwardButton.setText(label);
		forwardButton.setStyleName(style);
	}

	@UiHandler("forwardButton")
	public void onForwardButtonClicked(ClickEvent event){
		presenter.onFowardButtonClicked();
	}

	@UiHandler("backwardButton")
	public void onBackwardButtonClicked(ClickEvent event){
		presenter.onBackwardButtonClicked();
	}

	public void setFormTitle(String title) {
		this.title.setText(title);
	}

	public void showBackButton(boolean isVisible) {
		this.backwardButton.setVisible(isVisible);
	}

	public void showNextButton(boolean isVisible) {
		this.forwardButton.setVisible(isVisible);
	}

}
