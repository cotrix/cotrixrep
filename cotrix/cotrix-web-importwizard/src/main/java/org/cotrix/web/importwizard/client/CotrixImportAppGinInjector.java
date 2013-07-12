package org.cotrix.web.importwizard.client;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

import org.cotrix.web.importwizard.client.step.channel.AssetInfoDataProvider;
import org.cotrix.web.importwizard.client.step.upload.UploadStepPresenter;
import org.cotrix.web.importwizard.client.step.upload.UploadStepView;

@GinModules(CotrixImportAppGinModule.class)
public interface CotrixImportAppGinInjector extends Ginjector {
	
    public ImportServiceAsync getRpcService();
    
    public AssetInfoDataProvider getAssetInfoDataProvider();
    
    public UploadStepView getUploadForm();
    public UploadStepPresenter getUploadFormPresenter();
    public CotrixImportAppController getAppController();
    public ImportWizardView getImportWizardView();
    public ImportWizardPresenter getImportWizardPresenter();
}
