package org.cotrix.web.publish.client.wizard.step.codelistdetails;

import java.util.List;

import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.share.shared.codelist.UICodelistMetadata;
import org.cotrix.web.share.shared.codelist.UIAttribute;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.wizard.client.step.AbstractVisualWizardStep;

import com.allen_sauer.gwt.log.client.Log;
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
	
	protected UICodelistMetadata visualizedCodelist;
	
	@Inject
	public CodelistDetailsStepPresenterImpl(@PublishBus EventBus publishBus) {
		super("codelistDetails", TrackerLabels.SELECTION, "Codelist Details", "", PublishWizardStepButtons.BACKWARD);

		bind(publishBus);
	}
	
	protected void bind(EventBus publishBus)
	{
		publishBus.addHandler(ResetWizardEvent.TYPE, new ResetWizardHandler(){

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

	public void setCodelist(UICodelistMetadata codelistMetadata) {
		Log.trace("codelist codelistMetadata: "+codelistMetadata);
		
		configuration.setTitle(codelistMetadata.getName().getLocalPart()+" Details");

		view.setName(codelistMetadata.getName().getLocalPart());
		view.setVersion(codelistMetadata.getVersion());
		view.setState(codelistMetadata.getState().toString());
		List<UIAttribute> attributes = codelistMetadata.getAttributes();
		if (attributes.isEmpty()) view.setAttributesVisible(false);
		else {
			view.clearAttributes();
			for (UIAttribute attribute:attributes) view.addAttribute(attribute.getName().getLocalPart(), attribute.getValue());
			view.setAttributesVisible(true);
		}
	}
}
