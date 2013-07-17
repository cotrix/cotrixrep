package org.cotrix.web.importwizard.client;

import org.cotrix.web.importwizard.client.step.channel.AssetInfoDataProvider;
import org.cotrix.web.importwizard.client.step.channel.ChannelStepPresenter;
import org.cotrix.web.importwizard.client.step.channel.ChannelStepPresenterImpl;
import org.cotrix.web.importwizard.client.step.channel.ChannelStepView;
import org.cotrix.web.importwizard.client.step.channel.ChannelStepViewImpl;
import org.cotrix.web.importwizard.client.step.done.DoneStepPresenter;
import org.cotrix.web.importwizard.client.step.done.DoneStepPresenterImpl;
import org.cotrix.web.importwizard.client.step.done.DoneStepView;
import org.cotrix.web.importwizard.client.step.done.DoneStepViewImpl;
import org.cotrix.web.importwizard.client.step.mapping.MappingStepPresenter;
import org.cotrix.web.importwizard.client.step.mapping.MappingStepPresenterImpl;
import org.cotrix.web.importwizard.client.step.mapping.MappingStepFormView;
import org.cotrix.web.importwizard.client.step.mapping.MappingStepViewImpl;
import org.cotrix.web.importwizard.client.step.metadata.MetadataStepPresenter;
import org.cotrix.web.importwizard.client.step.metadata.MetadataStepPresenterImpl;
import org.cotrix.web.importwizard.client.step.metadata.MetadataStepView;
import org.cotrix.web.importwizard.client.step.metadata.MetadataStepViewImpl;
import org.cotrix.web.importwizard.client.step.preview.PreviewStepPresenter;
import org.cotrix.web.importwizard.client.step.preview.PreviewStepPresenterImpl;
import org.cotrix.web.importwizard.client.step.preview.PreviewStepView;
import org.cotrix.web.importwizard.client.step.preview.PreviewStepViewImpl;
import org.cotrix.web.importwizard.client.step.sourceselection.SourceSelectionStepPresenter;
import org.cotrix.web.importwizard.client.step.sourceselection.SourceSelectionStepPresenterImpl;
import org.cotrix.web.importwizard.client.step.sourceselection.SourceSelectionStepView;
import org.cotrix.web.importwizard.client.step.sourceselection.SourceSelectionStepViewImpl;
import org.cotrix.web.importwizard.client.step.summary.SummaryStepPresenter;
import org.cotrix.web.importwizard.client.step.summary.SummaryStepPresenterImpl;
import org.cotrix.web.importwizard.client.step.summary.SummaryStepView;
import org.cotrix.web.importwizard.client.step.summary.SummaryStepViewImpl;
import org.cotrix.web.importwizard.client.step.upload.UploadStepPresenter;
import org.cotrix.web.importwizard.client.step.upload.UploadStepPresenterImpl;
import org.cotrix.web.importwizard.client.step.upload.UploadStepView;
import org.cotrix.web.importwizard.client.step.upload.UploadStepViewImpl;
import org.cotrix.web.share.shared.CotrixImportModelController;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class CotrixImportAppGinModule extends AbstractGinModule {

    @Provides
    @Singleton
    public HandlerManager getEventBus() {
        return new HandlerManager(null);
    }
   
    @Provides
    @Singleton
    public CotrixImportModelController getModel() {
    	return new CotrixImportModelController();
    }

    protected void configure() {
        bind(CotrixImportAppController.class).to(CotrixImportAppControllerImpl.class);
        
        bind(ImportWizardView.class).to(ImportWizardViewImpl.class).in(Singleton.class);
        bind(ImportWizardPresenter.class).to(ImportWizardPresenterImpl.class);
        
        bind(SourceSelectionStepView.class).to(SourceSelectionStepViewImpl.class).asEagerSingleton();
        bind(SourceSelectionStepPresenter.class).to(SourceSelectionStepPresenterImpl.class).asEagerSingleton();
      
        bind(UploadStepView.class).to(UploadStepViewImpl.class);
        bind(UploadStepPresenter.class).to(UploadStepPresenterImpl.class);
        
        bind(ChannelStepView.class).to(ChannelStepViewImpl.class);
        bind(ChannelStepPresenter.class).to(ChannelStepPresenterImpl.class);
        bind(AssetInfoDataProvider.class).in(Singleton.class);
       // bind(AssetInfoDataProvider.class).to(AssetInfoDataProvider.class).in(Singleton.class);
     
        bind(MetadataStepView.class).to(MetadataStepViewImpl.class);
        bind(MetadataStepPresenter.class).to(MetadataStepPresenterImpl.class);
        
        bind(PreviewStepView.class).to(PreviewStepViewImpl.class);
        bind(PreviewStepPresenter.class).to(PreviewStepPresenterImpl.class);
        
        bind(MappingStepFormView.class).to(MappingStepViewImpl.class);
        bind(MappingStepPresenter.class).to(MappingStepPresenterImpl.class);
        
        bind(SummaryStepView.class).to(SummaryStepViewImpl.class);
        bind(SummaryStepPresenter.class).to(SummaryStepPresenterImpl.class);
      
        bind(DoneStepView.class).to(DoneStepViewImpl.class);
        bind(DoneStepPresenter.class).to(DoneStepPresenterImpl.class);
    }
}
