package org.cotrix.web.importwizard.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cotrix.web.importwizard.client.progresstracker.ProgressTracker;
import org.cotrix.web.importwizard.client.progresstracker.ProgressTracker.ProgressStep;
import org.cotrix.web.importwizard.client.step.VisualWizardStep;
import org.cotrix.web.importwizard.client.step.WizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImportWizardViewImpl extends ResizeComposite implements ImportWizardView {

	@UiTemplate("ImportWizard.ui.xml")
	interface ImportWizardUiBinder extends UiBinder<Widget, ImportWizardViewImpl> {}
	private static ImportWizardUiBinder uiBinder = GWT.create(ImportWizardUiBinder.class);

	@UiField ProgressTracker progressTracker;

	@UiField DeckLayoutPanel stepsPanel;

	@UiField Label title;
	@UiField Label subtitle;

	@UiField Button nextButton;
	@UiField Button backButton;
	@UiField Button newImportButton;
	@UiField Button importButton;
	@UiField Button manageButton;

	protected int currentIndex = 0;
	protected Map<String, Integer> decksIndexes;
	protected Map<String, Integer> labelsIndexes;

	private Presenter presenter;
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public ImportWizardViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		decksIndexes = new HashMap<String, Integer>();
	}

	public void addStep(VisualWizardStep step)
	{
		//Log.trace("Adding "+step.getId());
		step.go(stepsPanel);
		decksIndexes.put(step.getId(), currentIndex++);
		//Log.trace("Totalpanels in deck "+stepsPanel.getWidgetCount());

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
		backButton.setVisible(false);
	}

	public void showBackwardButton()
	{
		backButton.setVisible(true);
	}

	public void setBackwardButton(String label, String style)
	{
		backButton.setText(label);
		backButton.setStyleName(style);
	}

	public void hideForwardButton()
	{
		nextButton.setVisible(false);
	}

	public void showForwardButton()
	{
		nextButton.setVisible(true);
	}

	public void setForwardButton(String label, String style)
	{
		nextButton.setText(label);
		nextButton.setStyleName(style);
	}

	public void setFormTitle(String title) {
		this.title.setText(title);
	}

	public void showBackButton(boolean isVisible) {
		this.backButton.setVisible(isVisible);
	}

	public void showNextButton(boolean isVisible) {
		this.nextButton.setVisible(isVisible);
	}
	
	@UiHandler("nextButton")
	public void onNextButtonClicked(ClickEvent event){
		presenter.onButtonClicked(WizardButton.NEXT);
	}

	@UiHandler("backButton")
	public void onBackButtonClicked(ClickEvent event){
		presenter.onButtonClicked(WizardButton.BACK);
	}
	
	@UiHandler("importButton")
	public void onImportButtonClicked(ClickEvent event){
		presenter.onButtonClicked(WizardButton.IMPORT);
	}
	
	@UiHandler("newImportButton")
	public void onNewImportButtonClicked(ClickEvent event){
		presenter.onButtonClicked(WizardButton.NEW_IMPORT);
	}

	@UiHandler("manageButton")
	public void onManageButtonClicked(ClickEvent event){
		presenter.onButtonClicked(WizardButton.MANAGE);
	}

	@Override
	public void showButton(WizardButton wizardButton) {
		Button button = getButton(wizardButton);
		button.setVisible(true);
	}

	@Override
	public void hideButton(WizardButton wizardButton) {
		Button button = getButton(wizardButton);
		button.setVisible(false);
	}
	
	@Override
	public void hideAllButtons()
	{
		for (WizardButton button:WizardButton.values()) hideButton(button);
	}
	
	protected Button getButton(WizardButton button)
	{
		switch (button) {
			case BACK: return backButton;
			case IMPORT: return importButton;
			case NEW_IMPORT: return newImportButton;
			case MANAGE: return manageButton;
			case NEXT: return nextButton;
			default: {
				Log.fatal("Unknow button "+button);
				throw new IllegalArgumentException("Unknow button "+button);
			}
		}
	}

}
