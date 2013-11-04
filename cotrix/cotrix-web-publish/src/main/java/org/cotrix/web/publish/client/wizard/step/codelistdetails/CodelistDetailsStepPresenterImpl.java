package org.cotrix.web.publish.client.wizard.step.codelistdetails;

import org.cotrix.web.publish.client.PublishServiceAsync;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.share.client.wizard.step.AbstractVisualWizardStep;
import org.cotrix.web.share.shared.codelist.CodelistMetadata;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistDetailsStepPresenterImpl extends AbstractVisualWizardStep implements CodelistDetailsStepPresenter {

	@Inject
	protected CodelistDetailsStepView view;
	
	@Inject
	protected PublishServiceAsync importService;
	
	protected CodelistMetadata visualizedCodelist;
	
	@Inject
	public CodelistDetailsStepPresenterImpl(@PublishBus EventBus importEventBus) {
		super("codelistDetails", TrackerLabels.CODELIST_DETAILS, "Codelist Details", "", PublishWizardStepButtons.BACKWARD);

		bind(importEventBus);
	}
	
	protected void bind(EventBus importEventBus)
	{
		importEventBus.addHandler(ResetWizardEvent.TYPE, new ResetWizardHandler(){

			@Override
			public void onResetWizard(ResetWizardEvent event) {
			}});
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		return true;
	}


	public void setAsset(CodelistMetadata codelist) {
		Log.trace("getting codelist details for "+codelist);
		importService.getMetadata(codelist.getId(), new AsyncCallback<CodelistMetadata>() {
			
			@Override
			public void onSuccess(CodelistMetadata result) {
				view.setCodelist(result);
				visualizedCodelist = result;
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed loading codelist details", caught);
			}
		});
	}
}
