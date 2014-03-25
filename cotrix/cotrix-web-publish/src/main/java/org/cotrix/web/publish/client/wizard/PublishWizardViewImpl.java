package org.cotrix.web.publish.client.wizard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cotrix.web.common.client.error.ErrorManager;
import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.widgets.ProgressDialog;
import org.cotrix.web.wizard.client.progresstracker.ProgressTracker;
import org.cotrix.web.wizard.client.progresstracker.ProgressTracker.ProgressStep;
import org.cotrix.web.wizard.client.step.VisualWizardStep;

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
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class PublishWizardViewImpl extends ResizeComposite implements PublishWizardView {

	@UiTemplate("PublishWizard.ui.xml")
	interface PublishWizardUiBinder extends UiBinder<Widget, PublishWizardViewImpl> {}
	private static PublishWizardUiBinder uiBinder = GWT.create(PublishWizardUiBinder.class);

	@UiField ProgressTracker progressTracker;

	@UiField DeckLayoutPanel stepsPanel;

	@UiField Label title;
	@UiField Label subtitle;

	@UiField Button nextButton;
	@UiField Button backButton;
	@UiField Button newPublishButton;
	@UiField Button publishButton;

	protected int currentIndex = 0;
	protected Map<String, Integer> decksIndexes;
	protected Map<String, Integer> labelsIndexes;
	
	@Inject
	protected ProgressDialog progressDialog;
	
	@Inject
	protected ErrorManager errorManager;

	private Presenter presenter;
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public PublishWizardViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		decksIndexes = new HashMap<String, Integer>();
		CommonResources.INSTANCE.css().ensureInjected();
	}
	
	@Override
	public void showProgress() {
		progressDialog.showCentered();
	}

	@Override
	public void hideProgress() {
		if(progressDialog != null) progressDialog.hide();
	}

	public void addStep(VisualWizardStep step)
	{
		step.go(stepsPanel);
		decksIndexes.put(step.getId(), currentIndex++);
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

	public void showStep(VisualWizardStep step)
	{
		Integer deckIndex = decksIndexes.get(step.getId());
		if (deckIndex == null) throw new IllegalArgumentException("Step "+step.getId()+" not found in deck, forgot to register?");
		stepsPanel.showWidget(deckIndex);
	}

	public void showLabel(ProgressStep step)
	{
		progressTracker.setCurrentStep(step);
	}
	
	@UiHandler("nextButton")
	public void onNextButtonClicked(ClickEvent event){
		presenter.onButtonClicked(PublishWizardButton.NEXT);
	}

	@UiHandler("backButton")
	public void onBackButtonClicked(ClickEvent event){
		presenter.onButtonClicked(PublishWizardButton.BACK);
	}
	
	@UiHandler("publishButton")
	public void onPublishButtonClicked(ClickEvent event){
		presenter.onButtonClicked(PublishWizardButton.PUBLISH);
	}
	
	@UiHandler("newPublishButton")
	public void onNewPublishButtonClicked(ClickEvent event){
		presenter.onButtonClicked(PublishWizardButton.NEW_PUBLISH);
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
		for (PublishWizardButton button:PublishWizardButton.values()) hideButton(button);
	}
	
	protected Button getButton(WizardButton button)
	{
		assert button instanceof PublishWizardButton;
		PublishWizardButton importWizardButton = (PublishWizardButton) button;
		switch (importWizardButton) {
			case BACK: return backButton;
			case PUBLISH: return publishButton;
			case NEW_PUBLISH: return newPublishButton;
			case NEXT: return nextButton;
			default: {
				Log.fatal("Unknow button "+button);
				throw new IllegalArgumentException("Unknow button "+button);
			}
		}
	}

	@Override
	public void showError(Throwable throwable) {
		errorManager.showError(throwable);
	}

}
