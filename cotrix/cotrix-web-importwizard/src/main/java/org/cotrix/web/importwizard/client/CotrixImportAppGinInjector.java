package org.cotrix.web.importwizard.client;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import org.cotrix.web.importwizard.client.step.upload.UploadStepPresenter;
import org.cotrix.web.importwizard.client.step.upload.UploadStepView;

@GinModules(CotrixImportAppGinModule.class)
public interface CotrixImportAppGinInjector extends Ginjector {
    public UploadStepView getUploadForm();
    public ImportServiceAsync getRpcService();
    public UploadStepPresenter getUploadFormPresenter();
    public CotrixImportAppController getAppController();
    public ImportWizardView getImportWizardView();
    public ImportWizardPresenter getImportWizardPresenter();
}
