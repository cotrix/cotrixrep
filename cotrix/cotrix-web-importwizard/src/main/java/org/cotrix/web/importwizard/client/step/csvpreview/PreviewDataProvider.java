/**
 * 
 */
package org.cotrix.web.importwizard.client.step.csvpreview;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.step.csvpreview.PreviewGrid.DataProvider;
import org.cotrix.web.share.shared.CsvConfiguration;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PreviewDataProvider implements DataProvider {
	
	@Inject
	ImportServiceAsync service;
	
	CsvConfiguration configuration;

	/**
	 * @return the configuration
	 */
	public CsvConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * @param configuration the configuration to set
	 */
	public void setConfiguration(CsvConfiguration configuration) {
		this.configuration = configuration;
	}



	@Override
	public void getData(AsyncCallback<PreviewData> callback) {
		Log.trace("loading preview data with configuration "+configuration);
		service.getCsvPreviewData(configuration, callback);
	}

}
