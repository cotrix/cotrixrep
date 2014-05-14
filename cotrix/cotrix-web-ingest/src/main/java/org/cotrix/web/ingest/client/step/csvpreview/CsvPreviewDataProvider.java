/**
 * 
 */
package org.cotrix.web.ingest.client.step.csvpreview;

import java.util.List;

import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.ingest.client.IngestServiceAsync;
import org.cotrix.web.ingest.client.util.PreviewDataGrid.PreviewDataProvider;
import org.cotrix.web.ingest.shared.PreviewHeaders;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvPreviewDataProvider implements PreviewDataProvider {
	
	@Inject
	private IngestServiceAsync service;
	
	private CsvConfiguration configuration;

	@Override
	public void getHeaders(final AsyncCallback<PreviewHeaders> headersCallBack) {
		service.getPreviewHeaders(configuration, new ManagedFailureCallback<PreviewHeaders>() {

			@Override
			public void onSuccess(PreviewHeaders result) {
				headersCallBack.onSuccess(result);
			}
		});
	}

	@Override
	public void getData(Range range, final AsyncCallback<DataWindow<List<String>>> dataCallBack) {
		service.getPreviewData(range, new ManagedFailureCallback<DataWindow<List<String>>>() {

			@Override
			public void onSuccess(DataWindow<List<String>> result) {
				Log.trace("retrieved "+result);
				dataCallBack.onSuccess(result);
			}
		});			
		
	}
	
	public CsvConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(CsvConfiguration configuration) {
		this.configuration = configuration;		
	}

}
