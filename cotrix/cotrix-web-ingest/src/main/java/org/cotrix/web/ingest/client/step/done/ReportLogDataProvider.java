/**
 * 
 */
package org.cotrix.web.ingest.client.step.done;

import java.util.List;

import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.common.shared.ReportLog;
import org.cotrix.web.ingest.client.ImportServiceAsync;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class ReportLogDataProvider extends AsyncDataProvider<ReportLog> {
	
	@Inject
	protected ImportServiceAsync importService;
	
	/**
	 * @param importService
	 */
	public ReportLogDataProvider() {
	}

	@Override
	protected void onRangeChanged(HasData<ReportLog> display) {
		final Range range = display.getVisibleRange();
		Log.trace("onRangeChanged range: "+range);
		importService.getReportLogs(range, new ManagedFailureCallback<DataWindow<ReportLog>>() {
			
			@Override
			public void onSuccess(DataWindow<ReportLog> batch) {
				List<ReportLog> logs = batch.getData();
				updateRowCount(batch.getTotalSize(), true);
				updateRowData(range.getStart(), logs);
			}
		});
	}

}
