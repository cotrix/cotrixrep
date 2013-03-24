package org.cotrix.web.importwizard.client;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import org.cotrix.web.importwizard.client.presenter.ImportWizardPresenter;
import org.cotrix.web.importwizard.client.presenter.UploadFormPresenter;
import org.cotrix.web.importwizard.client.view.ImportWizardView;
import org.cotrix.web.importwizard.client.view.form.UploadFormView;

@GinModules(CotrixImportAppGinModule.class)
public interface CotrixImportAppGinInjector extends Ginjector {
    public UploadFormView getUploadForm();
    public ImportServiceAsync getRpcService();
    public UploadFormPresenter getUploadFormPresenter();
    public CotrixImportAppController getAppController();
    public ImportWizardView getImportWizardView();
    public ImportWizardPresenter getImportWizardPresenter();
}
