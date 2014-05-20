/**
 * 
 */
package org.cotrix.web.ingest.client.step.csvpreview;

import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.ingest.client.IngestServiceAsync;
import org.cotrix.web.ingest.client.step.csvpreview.PreviewGrid.DataProvider;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class PreviewDataProvider implements DataProvider {
	
	@Inject
	IngestServiceAsync service;
	
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
