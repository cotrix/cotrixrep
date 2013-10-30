package org.cotrix.web.importwizard.client;
import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

import org.cotrix.web.importwizard.client.step.selection.AssetInfoDataProvider;
import org.cotrix.web.importwizard.client.step.upload.UploadStepPresenter;
import org.cotrix.web.importwizard.client.step.upload.UploadStepView;
import org.cotrix.web.importwizard.client.wizard.ImportWizardPresenter;
import org.cotrix.web.importwizard.client.wizard.ImportWizardView;
import org.cotrix.web.share.client.CommonGinModule;

@GinModules({CotrixImportAppGinModule.class, CommonGinModule.class})
public interface CotrixImportAppGinInjector extends Ginjector {
	
	public static CotrixImportAppGinInjector INSTANCE = GWT.create(CotrixImportAppGinInjector.class);
	
    public ImportServiceAsync getRpcService();
    
    public AssetInfoDataProvider getAssetInfoDataProvider();
    
    public UploadStepView getUploadForm();
    public UploadStepPresenter getUploadFormPresenter();
    public ImportWizardController getController();
    public ImportWizardView getImportWizardView();
    public ImportWizardPresenter getImportWizardPresenter();
}
