/**
 * 
 */
package org.cotrix.web.codelistmanager.client.view;

import java.util.List;

import org.cotrix.web.codelistmanager.client.CotrixManagerAppGinInjector;
import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.shared.UICodeListRow;
import org.cotrix.web.share.shared.DataWindow;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListRowDataProvider extends AsyncDataProvider<UICodeListRow> {
	
	protected ManagerServiceAsync managerService = CotrixManagerAppGinInjector.INSTANCE.getRpcService();
	
	protected String codelistId;

	/**
	 * @param codelistId
	 */
	public CodeListRowDataProvider(String codelistId) {
		this.codelistId = codelistId;
	}

	@Override
	protected void onRangeChanged(HasData<UICodeListRow> display) {
		

		final Range range = display.getVisibleRange();
		
		managerService.getCodelistRows(codelistId, range, new AsyncCallback<DataWindow<UICodeListRow>>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Codelist retrieving failed", caught);
			}

			@Override
			public void onSuccess(DataWindow<UICodeListRow> result) {
				List<UICodeListRow> rows = result.getData();
				Log.trace("loaded "+rows.size()+" rows");
				updateRowCount(result.getTotalSize(), true);
				updateRowData(range.getStart(), rows);
			}
		});
		
	}

}
