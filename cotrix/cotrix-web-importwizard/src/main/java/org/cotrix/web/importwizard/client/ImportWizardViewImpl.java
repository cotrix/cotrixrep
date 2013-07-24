package org.cotrix.web.importwizard.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cotrix.web.importwizard.client.progresstracker.ProgressTracker;
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
	
	@UiField Label titlePanel;
	
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
	
	public void setLabels(List<WizardStep> steps) {
		Log.trace("setting "+steps.size()+" labels");
		List<String> labels = new ArrayList<String>();
		labelsIndexes = new HashMap<String, Integer>();
		int index = 0;
		for (WizardStep step:steps){
			String label = step.getConfiguration().getLabel();
			labels.add(label);
			labelsIndexes.put(step.getId(), index++);
		}
		progressTracker.init(labels);
	}
	
	public void setStepTitle(String title)
	{
		this.titlePanel.setText(title);
	}
	
	public void showStep(WizardStep step)
	{
		Log.trace("showStep "+step.getId()+" steps: "+stepsPanel.getWidgetCount());
		int deckIndex = decksIndexes.get(step.getId());
		stepsPanel.showWidget(deckIndex);
		showLabel(step);
	}
	
	public void showLabel(WizardStep step)
	{
		int labelIndex = labelsIndexes.get(step.getId());
		progressTracker.setCurrentStep(labelIndex);
	}
	
	public void hideBackwardButton()
	{
		backwardButton.setVisible(false);
	}
	
	public void showBackwardButton()
	{
		backwardButton.setVisible(true);
	}
	
	public void setBackwardButtonLabel(String label)
	{
		backwardButton.setText(label);
	}
	
	public void hideForwardButton()
	{
		forwardButton.setVisible(false);
	}
	
	public void showForwardButton()
	{
		forwardButton.setVisible(true);
	}
	
	public void setForwardButtonLabel(String label)
	{
		forwardButton.setText(label);
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
		this.titlePanel.setText(title);
	}

	public void showBackButton(boolean isVisible) {
		this.backwardButton.setVisible(isVisible);
	}

	public void showNextButton(boolean isVisible) {
		this.forwardButton.setVisible(isVisible);
	}

}
