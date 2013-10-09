/**
 * 
 */
package org.cotrix.web.codelistmanager.client.codelist;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.client.codelist.event.AttributeSetChangedEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.AttributeSetChangedEvent.AttributeSetChangedHandler;
import org.cotrix.web.codelistmanager.client.codelist.event.AttributeSetChangedEvent.HasAttributeSetChangedHandlers;
import org.cotrix.web.codelistmanager.shared.UICodelistRow;
import org.cotrix.web.share.shared.DataWindow;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistRowDataProvider extends AsyncDataProvider<UICodelistRow> implements HasAttributeSetChangedHandlers {
	
	protected HandlerManager handlerManager = new HandlerManager(this);
	
	@Inject
	protected ManagerServiceAsync managerService;
	
	@Inject @CodelistId
	protected String codelistId;

	@Override
	protected void onRangeChanged(HasData<UICodelistRow> display) {
		

		final Range range = display.getVisibleRange();
		
		managerService.getCodelistRows(codelistId, range, new AsyncCallback<DataWindow<UICodelistRow>>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Codelist retrieving failed", caught);
			}

			@Override
			public void onSuccess(DataWindow<UICodelistRow> result) {
				List<UICodelistRow> rows = result.getData();
				Log.trace("loaded "+rows.size()+" rows");
				checkAttributeSet(rows);
				updateRowCount(result.getTotalSize(), true);
				updateRowData(range.getStart(), rows);
			}
		});
		
	}
	
	protected void checkAttributeSet(List<UICodelistRow> rows)
	{
		if (handlerManager.getHandlerCount(AttributeSetChangedEvent.TYPE) == 0) return;
		Set<String> attributesNames = new HashSet<String>();
		for (UICodelistRow row:rows) attributesNames.addAll(row.getAttributesNames());
		handlerManager.fireEvent(new AttributeSetChangedEvent(attributesNames));
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	@Override
	public HandlerRegistration addAttributeSetChangedHandler(AttributeSetChangedHandler handler) {
		return handlerManager.addHandler(AttributeSetChangedEvent.TYPE, handler);
	}

}
