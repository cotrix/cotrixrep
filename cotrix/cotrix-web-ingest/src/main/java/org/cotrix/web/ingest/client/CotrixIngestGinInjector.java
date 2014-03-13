package org.cotrix.web.ingest.client;
import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

import org.cotrix.web.common.client.CommonGinModule;
import org.cotrix.web.ingest.client.IngestServiceAsync;
import org.cotrix.web.ingest.client.step.selection.AssetInfoDataProvider;
import org.cotrix.web.ingest.client.step.upload.UploadStepPresenter;
import org.cotrix.web.ingest.client.step.upload.UploadStepView;
import org.cotrix.web.ingest.client.wizard.ImportWizardPresenter;
import org.cotrix.web.ingest.client.wizard.ImportWizardView;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@GinModules({CotrixIngestGinModule.class, CommonGinModule.class})
public interface CotrixIngestGinInjector extends Ginjector {
	
	public static CotrixIngestGinInjector INSTANCE = GWT.create(CotrixIngestGinInjector.class);
	
    public IngestServiceAsync getRpcService();
    
    public AssetInfoDataProvider getAssetInfoDataProvider();
    
    public UploadStepView getUploadForm();
    public UploadStepPresenter getUploadFormPresenter();
    public IngestController getController();
    public ImportWizardView getImportWizardView();
    public ImportWizardPresenter getImportWizardPresenter();
}
