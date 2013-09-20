/**
 * 
 */
package org.cotrix.web.importwizard.client.step.done;

import java.util.List;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.shared.ReportLog;
import org.cotrix.web.share.shared.DataWindow;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
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
		importService.getReportLogs(range, new AsyncCallback<DataWindow<ReportLog>>() {
			
			@Override
			public void onSuccess(DataWindow<ReportLog> batch) {
				List<ReportLog> logs = batch.getData();
				Log.trace("loaded "+logs.size()+" logs");
				updateRowCount(batch.getTotalSize(), true);
				updateRowData(range.getStart(), logs);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				//TODO show the error to the user?
				Log.error("An error occurred loading the logs", caught);
			}
		});
	}

}
