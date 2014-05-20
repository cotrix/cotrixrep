/**
 * 
 */
package org.cotrix.web.publish.client.wizard;

import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.event.PublishCompleteEvent;
import org.cotrix.web.publish.shared.DownloadType;
import org.cotrix.web.wizard.client.WizardAction;
import org.cotrix.web.wizard.client.WizardActionHandler;
import org.cotrix.web.wizard.client.WizardController;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.event.ResetWizardEvent.ResetWizardHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class PublishWizardActionHandler implements WizardActionHandler {
	
	protected static final String REPORT_DOWNLOAD_URL = GWT.getModuleBaseURL()+"service/publishDownload?"+DownloadType.PARAMETER_NAME+"="+DownloadType.REPORT;

	@Inject
	@PublishBus
	protected EventBus publishBus;
	private String codelistDownloadUrl;
		
	
	@Inject
	private void bind() {
		publishBus.addHandler(PublishCompleteEvent.TYPE, new PublishCompleteEvent.PublishCompleteHandler() {
			
			@Override
			public void onPublishComplete(PublishCompleteEvent event) {
				codelistDownloadUrl = event.getDownloadUrl();
			}
		});
		publishBus.addHandler(ResetWizardEvent.TYPE, new ResetWizardHandler(){

			@Override
			public void onResetWizard(ResetWizardEvent event) {
				codelistDownloadUrl = null;
			}});
	}

	@Override
	public boolean handle(WizardAction action, WizardController controller) {
		if (action instanceof PublishWizardAction) {
			PublishWizardAction importWizardAction = (PublishWizardAction)action;
			switch (importWizardAction) {
				case NEW_PUBLISH: publishBus.fireEvent(new ResetWizardEvent()); break;
				case DOWNLOAD_CODELIST: if (codelistDownloadUrl!=null) Window.open(codelistDownloadUrl, "_blank", "");break;
				case DOWNLOAD_REPORT: Window.open(REPORT_DOWNLOAD_URL, "_blank", ""); break;
			}
		}
		return false;
	}

}
