package org.cotrix.web.publish.client;

import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardPresenter;
import org.cotrix.web.publish.client.wizard.PublishWizardPresenterImpl;
import org.cotrix.web.publish.client.wizard.PublishWizardView;
import org.cotrix.web.publish.client.wizard.PublishWizardViewImpl;
import org.cotrix.web.publish.client.wizard.step.DestinationNodeSelector;
import org.cotrix.web.publish.client.wizard.step.codelistdetails.CodelistDetailsStepPresenter;
import org.cotrix.web.publish.client.wizard.step.codelistdetails.CodelistDetailsStepPresenterImpl;
import org.cotrix.web.publish.client.wizard.step.codelistdetails.CodelistDetailsStepView;
import org.cotrix.web.publish.client.wizard.step.codelistdetails.CodelistDetailsStepViewImpl;
import org.cotrix.web.publish.client.wizard.step.codelistselection.CodelistSelectionStepPresenter;
import org.cotrix.web.publish.client.wizard.step.codelistselection.CodelistSelectionStepPresenterImpl;
import org.cotrix.web.publish.client.wizard.step.codelistselection.CodelistSelectionStepView;
import org.cotrix.web.publish.client.wizard.step.codelistselection.CodelistSelectionStepViewImpl;
import org.cotrix.web.publish.client.wizard.step.csvconfiguration.CsvConfigurationStepPresenter;
import org.cotrix.web.publish.client.wizard.step.csvconfiguration.CsvConfigurationStepPresenterImpl;
import org.cotrix.web.publish.client.wizard.step.csvconfiguration.CsvConfigurationStepView;
import org.cotrix.web.publish.client.wizard.step.csvconfiguration.CsvConfigurationStepViewImpl;
import org.cotrix.web.publish.client.wizard.step.csvmapping.CsvMappingStepPresenter;
import org.cotrix.web.publish.client.wizard.step.csvmapping.CsvMappingStepPresenterImpl;
import org.cotrix.web.publish.client.wizard.step.csvmapping.CsvMappingStepView;
import org.cotrix.web.publish.client.wizard.step.csvmapping.CsvMappingStepViewImpl;
import org.cotrix.web.publish.client.wizard.step.destinationselection.DestinationSelectionStepPresenter;
import org.cotrix.web.publish.client.wizard.step.destinationselection.DestinationSelectionStepPresenterImpl;
import org.cotrix.web.publish.client.wizard.step.destinationselection.DestinationSelectionStepView;
import org.cotrix.web.publish.client.wizard.step.destinationselection.DestinationSelectionStepViewImpl;
import org.cotrix.web.publish.client.wizard.step.done.DoneStepPresenter;
import org.cotrix.web.publish.client.wizard.step.done.DoneStepPresenterImpl;
import org.cotrix.web.publish.client.wizard.step.done.DoneStepView;
import org.cotrix.web.publish.client.wizard.step.done.DoneStepViewImpl;
import org.cotrix.web.publish.client.wizard.step.repositoryselection.RepositorySelectionStepPresenter;
import org.cotrix.web.publish.client.wizard.step.repositoryselection.RepositorySelectionStepPresenterImpl;
import org.cotrix.web.publish.client.wizard.step.repositoryselection.RepositorySelectionStepView;
import org.cotrix.web.publish.client.wizard.step.repositoryselection.RepositorySelectionStepViewImpl;
import org.cotrix.web.publish.client.wizard.step.sdmxmapping.SdmxMappingStepPresenter;
import org.cotrix.web.publish.client.wizard.step.sdmxmapping.SdmxMappingStepPresenterImpl;
import org.cotrix.web.publish.client.wizard.step.sdmxmapping.SdmxMappingStepView;
import org.cotrix.web.publish.client.wizard.step.sdmxmapping.SdmxMappingStepViewImpl;
import org.cotrix.web.publish.client.wizard.step.summary.SummaryStepPresenter;
import org.cotrix.web.publish.client.wizard.step.summary.SummaryStepPresenterImpl;
import org.cotrix.web.publish.client.wizard.step.summary.SummaryStepView;
import org.cotrix.web.publish.client.wizard.step.summary.SummaryStepViewImpl;
import org.cotrix.web.publish.client.wizard.step.typeselection.TypeSelectionStepPresenter;
import org.cotrix.web.publish.client.wizard.step.typeselection.TypeSelectionStepPresenterImpl;
import org.cotrix.web.publish.client.wizard.step.typeselection.TypeSelectionStepView;
import org.cotrix.web.publish.client.wizard.step.typeselection.TypeSelectionStepViewImpl;

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
	    
	   	bind(PublishController.class).in(Singleton.class);
	   	
		bind(CotrixPublishAppController.class).to(CotrixPublishAppControllerImpl.class);

		bind(PublishWizardPresenter.class).to(PublishWizardPresenterImpl.class);
		bind(PublishWizardView.class).to(PublishWizardViewImpl.class);
		
		bind(CodelistSelectionStepPresenter.class).to(CodelistSelectionStepPresenterImpl.class);
		bind(CodelistSelectionStepView.class).to(CodelistSelectionStepViewImpl.class);
		
		bind(CodelistDetailsStepPresenter.class).to(CodelistDetailsStepPresenterImpl.class);
		bind(CodelistDetailsStepView.class).to(CodelistDetailsStepViewImpl.class);
		
		bind(DestinationSelectionStepPresenter.class).to(DestinationSelectionStepPresenterImpl.class);
		bind(DestinationSelectionStepView.class).to(DestinationSelectionStepViewImpl.class);
		bind(DestinationNodeSelector.class).in(Singleton.class);
		
		bind(TypeSelectionStepPresenter.class).to(TypeSelectionStepPresenterImpl.class);
		bind(TypeSelectionStepView.class).to(TypeSelectionStepViewImpl.class);
		
		bind(CsvConfigurationStepPresenter.class).to(CsvConfigurationStepPresenterImpl.class);
		bind(CsvConfigurationStepView.class).to(CsvConfigurationStepViewImpl.class);
		
		bind(CsvMappingStepPresenter.class).to(CsvMappingStepPresenterImpl.class);
		bind(CsvMappingStepView.class).to(CsvMappingStepViewImpl.class);
		
		bind(SdmxMappingStepPresenter.class).to(SdmxMappingStepPresenterImpl.class);
		bind(SdmxMappingStepView.class).to(SdmxMappingStepViewImpl.class);
		
		bind(RepositorySelectionStepPresenter.class).to(RepositorySelectionStepPresenterImpl.class);
		bind(RepositorySelectionStepView.class).to(RepositorySelectionStepViewImpl.class);
		
		bind(SummaryStepPresenter.class).to(SummaryStepPresenterImpl.class);
		bind(SummaryStepView.class).to(SummaryStepViewImpl.class);
		
		bind(DoneStepPresenter.class).to(DoneStepPresenterImpl.class);
		bind(DoneStepView.class).to(DoneStepViewImpl.class);
	}

}
