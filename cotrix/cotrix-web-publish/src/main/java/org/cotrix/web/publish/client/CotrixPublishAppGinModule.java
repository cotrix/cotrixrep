package org.cotrix.web.publish.client;

import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardPresenter;
import org.cotrix.web.publish.client.wizard.PublishWizardPresenterImpl;
import org.cotrix.web.publish.client.wizard.PublishWizardView;
import org.cotrix.web.publish.client.wizard.PublishWizardViewImpl;
import org.cotrix.web.publish.client.wizard.step.codelistselection.CodelistSelectionStepPresenter;
import org.cotrix.web.publish.client.wizard.step.codelistselection.CodelistSelectionStepPresenterImpl;
import org.cotrix.web.publish.client.wizard.step.codelistselection.CodelistSelectionStepView;
import org.cotrix.web.publish.client.wizard.step.codelistselection.CodelistSelectionStepViewImpl;
import org.cotrix.web.publish.client.wizard.step.destinationselection.DestinationSelectionStepPresenter;
import org.cotrix.web.publish.client.wizard.step.destinationselection.DestinationSelectionStepPresenterImpl;
import org.cotrix.web.publish.client.wizard.step.destinationselection.DestinationSelectionStepView;
import org.cotrix.web.publish.client.wizard.step.destinationselection.DestinationSelectionStepViewImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixPublishAppGinModule extends AbstractGinModule {
    
	@Override
	protected void configure() {
	   	bind(EventBus.class).annotatedWith(PublishBus.class).to(SimpleEventBus.class).in(Singleton.class);
	    
	   	
		bind(CotrixPublishAppController.class).to(CotrixPublishAppControllerImpl.class);

		bind(PublishWizardPresenter.class).to(PublishWizardPresenterImpl.class);
		bind(PublishWizardView.class).to(PublishWizardViewImpl.class);
		
		bind(CodelistSelectionStepPresenter.class).to(CodelistSelectionStepPresenterImpl.class);
		bind(CodelistSelectionStepView.class).to(CodelistSelectionStepViewImpl.class);
		
		bind(DestinationSelectionStepPresenter.class).to(DestinationSelectionStepPresenterImpl.class);
		bind(DestinationSelectionStepView.class).to(DestinationSelectionStepViewImpl.class);
	}

}
