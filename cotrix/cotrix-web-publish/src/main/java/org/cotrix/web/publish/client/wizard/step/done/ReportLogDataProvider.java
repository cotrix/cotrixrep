/**
 * 
 */
package org.cotrix.web.publish.client.wizard.step.done;

import java.util.List;

import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.common.shared.ReportLog;
import org.cotrix.web.publish.client.PublishServiceAsync;

import com.allen_sauer.gwt.log.client.Log;
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
	protected PublishServiceAsync importService;
	
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
				Log.trace("loaded "+logs.size()+" logs");
				updateRowCount(batch.getTotalSize(), true);
				updateRowData(range.getStart(), logs);
			}
		});
	}

}
