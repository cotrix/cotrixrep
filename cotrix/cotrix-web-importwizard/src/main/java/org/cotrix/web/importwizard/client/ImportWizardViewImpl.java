package org.cotrix.web.importwizard.client;

import java.util.ArrayList;
import java.util.List;

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
	@UiField DeckPanel stepsPanel;
	@UiField Label titlePanel;
	
	@UiField Button forwardButton;
	@UiField Button backwardButton;
	@UiField Button uploadOtherButton;
	@UiField Button manageCodelistButton;

	private ProgressTracker progressTracker;

	private Presenter presenter;
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public ImportWizardViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		progressTracker = new ProgressTracker();
		progressTrackerPanel.add(progressTracker);
	}
	
	public void addSteps(List<WizardStep> steps)
	{
		Log.trace("Adding "+steps.size()+" steps");
		List<String> labels = new ArrayList<String>();
		for (WizardStep step:steps){
			step.go(stepsPanel);
			String label = step.getConfiguration().getLabel();
			labels.add(label);
			Log.trace("Added step "+label+" Panels in deck "+stepsPanel.getWidgetCount());
		}
		progressTracker.init(labels);
		
		Log.trace("Totalpanels in deck "+stepsPanel.getWidgetCount());
		
		stepsPanel.showWidget(0);
	}
	
	public void setStepTitle(String title)
	{
		this.titlePanel.setText(title);
	}
	
	public void showStep(int stepIndex)
	{
		Log.trace("showStep "+stepIndex+" steps: "+stepsPanel.getWidgetCount());
		stepsPanel.showWidget(stepIndex);
		progressTracker.setCurrentStep(stepIndex);
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
	

	/*public void initProgressBarTracker(){

	}

	public void initForm(){
		presenter.initForm(stepsPanel);
		stepsPanel.showWidget(0);
	}

	public void showNextStep(int index) {
		stepsPanel.showWidget(index + 1);
		//progressTracker.setActiveIndex(index + 2);		
	}

	public void showPrevStep(int index) {
		stepsPanel.showWidget(index -1);
		//progressTracker.setActiveIndex(index);
	}*/
	
	
	
	@UiHandler("forwardButton")
	public void onForwardButtonClicked(ClickEvent event){
		presenter.onFowardButtonClicked();
	}
	
	@UiHandler("backwardButton")
	public void onBackwardButtonClicked(ClickEvent event){
		presenter.onBackwardButtonClicked();
	}
	
	@UiHandler("uploadOtherButton")
	public void onUploadOtherButtonClicked(ClickEvent event) {
		presenter.onUploadOtherButtonClicked();
	}

	@UiHandler("manageCodelistButton")
	public void onManageCodelistButtonClicked(ClickEvent event) {
		presenter.onManageCodelistButtonClicked();
	}

	/*public void addForm() {
		presenter.addForm(contentPanel);
	}*/

	public void setFormTitle(String title) {
		this.titlePanel.setText(title);
	}

	public void showBackButton(boolean isVisible) {
		this.backwardButton.setVisible(isVisible);
	}

	public void showNextButton(boolean isVisible) {
		this.forwardButton.setVisible(isVisible);
	}

	public void showUploadOtherButton(boolean isVisible) {
		this.uploadOtherButton.setVisible(isVisible);
	}

	public void showManageCodelistButton(boolean isVisible) {
		this.manageCodelistButton.setVisible(isVisible);
	}

}
