package org.cotrix.web.ingest.client;

import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.step.DetailsNodeSelector;
import org.cotrix.web.ingest.client.step.MappingNodeSelector;
import org.cotrix.web.ingest.client.step.SourceNodeSelector;
import org.cotrix.web.ingest.client.step.codelistdetails.CodelistDetailsStepPresenter;
import org.cotrix.web.ingest.client.step.codelistdetails.CodelistDetailsStepPresenterImpl;
import org.cotrix.web.ingest.client.step.codelistdetails.CodelistDetailsStepView;
import org.cotrix.web.ingest.client.step.codelistdetails.CodelistDetailsStepViewImpl;
import org.cotrix.web.ingest.client.step.csvmapping.CsvMappingStepPresenter;
import org.cotrix.web.ingest.client.step.csvmapping.CsvMappingStepPresenterImpl;
import org.cotrix.web.ingest.client.step.csvmapping.CsvMappingStepView;
import org.cotrix.web.ingest.client.step.csvmapping.CsvMappingStepViewImpl;
import org.cotrix.web.ingest.client.step.csvpreview.CsvPreviewStepPresenter;
import org.cotrix.web.ingest.client.step.csvpreview.CsvPreviewStepPresenterImpl;
import org.cotrix.web.ingest.client.step.csvpreview.CsvPreviewStepView;
import org.cotrix.web.ingest.client.step.csvpreview.CsvPreviewStepViewImpl;
import org.cotrix.web.ingest.client.step.csvpreview.PreviewDataProvider;
import org.cotrix.web.ingest.client.step.done.DoneStepPresenter;
import org.cotrix.web.ingest.client.step.done.DoneStepPresenterImpl;
import org.cotrix.web.ingest.client.step.done.DoneStepView;
import org.cotrix.web.ingest.client.step.done.DoneStepViewImpl;
import org.cotrix.web.ingest.client.step.done.ReportLogDataProvider;
import org.cotrix.web.ingest.client.step.repositorydetails.RepositoryDetailsStepPresenter;
import org.cotrix.web.ingest.client.step.repositorydetails.RepositoryDetailsStepPresenterImpl;
import org.cotrix.web.ingest.client.step.repositorydetails.RepositoryDetailsStepView;
import org.cotrix.web.ingest.client.step.repositorydetails.RepositoryDetailsStepViewImpl;
import org.cotrix.web.ingest.client.step.sdmxmapping.SdmxMappingStepPresenter;
import org.cotrix.web.ingest.client.step.sdmxmapping.SdmxMappingStepPresenterImpl;
import org.cotrix.web.ingest.client.step.sdmxmapping.SdmxMappingStepView;
import org.cotrix.web.ingest.client.step.sdmxmapping.SdmxMappingStepViewImpl;
import org.cotrix.web.ingest.client.step.selection.AssetInfoDataProvider;
import org.cotrix.web.ingest.client.step.selection.SelectionStepPresenter;
import org.cotrix.web.ingest.client.step.selection.SelectionStepPresenterImpl;
import org.cotrix.web.ingest.client.step.selection.SelectionStepView;
import org.cotrix.web.ingest.client.step.selection.SelectionStepViewImpl;
import org.cotrix.web.ingest.client.step.sourceselection.SourceSelectionStepPresenter;
import org.cotrix.web.ingest.client.step.sourceselection.SourceSelectionStepPresenterImpl;
import org.cotrix.web.ingest.client.step.sourceselection.SourceSelectionStepView;
import org.cotrix.web.ingest.client.step.sourceselection.SourceSelectionStepViewImpl;
import org.cotrix.web.ingest.client.step.summary.SummaryStepPresenter;
import org.cotrix.web.ingest.client.step.summary.SummaryStepPresenterImpl;
import org.cotrix.web.ingest.client.step.summary.SummaryStepView;
import org.cotrix.web.ingest.client.step.summary.SummaryStepViewImpl;
import org.cotrix.web.ingest.client.step.upload.UploadStepPresenter;
import org.cotrix.web.ingest.client.step.upload.UploadStepPresenterImpl;
import org.cotrix.web.ingest.client.step.upload.UploadStepView;
import org.cotrix.web.ingest.client.step.upload.UploadStepViewImpl;
import org.cotrix.web.ingest.client.task.ImportTask;
import org.cotrix.web.ingest.client.task.RetrieveAssetTask;
import org.cotrix.web.ingest.client.wizard.ImportWizardPresenter;
import org.cotrix.web.ingest.client.wizard.ImportWizardPresenterImpl;
import org.cotrix.web.ingest.client.wizard.ImportWizardView;
import org.cotrix.web.ingest.client.wizard.ImportWizardViewImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class CotrixImportAppGinModule extends AbstractGinModule {

    protected void configure() {
    	bind(EventBus.class).annotatedWith(ImportBus.class).to(SimpleEventBus.class).in(Singleton.class);
    	
        bind(ImportWizardController.class).to(ImportWizardControllerImpl.class);
        
        bind(ImportWizardView.class).to(ImportWizardViewImpl.class).in(Singleton.class);
        bind(ImportWizardPresenter.class).to(ImportWizardPresenterImpl.class);
        bind(SourceNodeSelector.class).in(Singleton.class);
        
        bind(SourceSelectionStepView.class).to(SourceSelectionStepViewImpl.class).asEagerSingleton();
        bind(SourceSelectionStepPresenter.class).to(SourceSelectionStepPresenterImpl.class).asEagerSingleton();
      
        bind(UploadStepView.class).to(UploadStepViewImpl.class);
        bind(UploadStepPresenter.class).to(UploadStepPresenterImpl.class).in(Singleton.class);
        
        bind(SelectionStepView.class).to(SelectionStepViewImpl.class);
        bind(SelectionStepPresenter.class).to(SelectionStepPresenterImpl.class).in(Singleton.class);
        bind(AssetInfoDataProvider.class).in(Singleton.class);
        bind(DetailsNodeSelector.class).in(Singleton.class);
        
        bind(RetrieveAssetTask.class).in(Singleton.class);
        bind(MappingNodeSelector.class).in(Singleton.class);
        
        bind(CodelistDetailsStepView.class).to(CodelistDetailsStepViewImpl.class);
        bind(CodelistDetailsStepPresenter.class).to(CodelistDetailsStepPresenterImpl.class).in(Singleton.class);
        
        bind(RepositoryDetailsStepView.class).to(RepositoryDetailsStepViewImpl.class);
        bind(RepositoryDetailsStepPresenter.class).to(RepositoryDetailsStepPresenterImpl.class).in(Singleton.class);
        
        bind(CsvPreviewStepView.class).to(CsvPreviewStepViewImpl.class);
        bind(CsvPreviewStepPresenter.class).to(CsvPreviewStepPresenterImpl.class);
        bind(PreviewDataProvider.class).in(Singleton.class);
        
        bind(CsvMappingStepView.class).to(CsvMappingStepViewImpl.class);
        bind(CsvMappingStepPresenter.class).to(CsvMappingStepPresenterImpl.class);
        
        bind(SdmxMappingStepView.class).to(SdmxMappingStepViewImpl.class);
        bind(SdmxMappingStepPresenter.class).to(SdmxMappingStepPresenterImpl.class);
        
        bind(SummaryStepView.class).to(SummaryStepViewImpl.class);
        bind(SummaryStepPresenter.class).to(SummaryStepPresenterImpl.class);
        
        bind(ImportTask.class).in(Singleton.class);
      
        bind(DoneStepView.class).to(DoneStepViewImpl.class);
        bind(DoneStepPresenter.class).to(DoneStepPresenterImpl.class);
        bind(ReportLogDataProvider.class).in(Singleton.class);
    }
}
