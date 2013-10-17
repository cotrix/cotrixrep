/**
 * 
 */
package org.cotrix.web.codelistmanager.client.codelist;

import java.util.List;
import java.util.Set;

import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.client.codelist.attribute.Group;
import org.cotrix.web.codelistmanager.client.codelist.attribute.GroupFactory;
import org.cotrix.web.codelistmanager.client.codelist.event.GroupsChangedEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.GroupsChangedEvent.GroupsChangedHandler;
import org.cotrix.web.codelistmanager.client.codelist.event.GroupsChangedEvent.HasGroupsChangedHandlers;
import org.cotrix.web.codelistmanager.shared.UICode;
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
public class CodelistCodesProvider extends AsyncDataProvider<UICode> implements HasGroupsChangedHandlers {
	
	protected HandlerManager handlerManager = new HandlerManager(this);
	
	protected Range lastRange;
	protected List<UICode> codes;
	
	@Inject
	protected ManagerServiceAsync managerService;
	
	@Inject @CodelistId
	protected String codelistId;

	@Override
	protected void onRangeChanged(HasData<UICode> display) {
		

		final Range range = display.getVisibleRange();
		
		managerService.getCodelistCodes(codelistId, range, new AsyncCallback<DataWindow<UICode>>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Codelist retrieving failed", caught);
			}

			@Override
			public void onSuccess(DataWindow<UICode> result) {
				codes = result.getData();
				Log.trace("loaded "+codes.size()+" rows");
				checkGroups(codes);
				updateRowCount(result.getTotalSize(), true);
				updateRowData(range.getStart(), codes);
				lastRange = range;
			}
		});
		
	}
	
	protected void checkGroups(List<UICode> rows)
	{
		Set<Group> groups = GroupFactory.getGroups(rows);
		handlerManager.fireEvent(new GroupsChangedEvent(groups));
	}
	
	public void refresh()
	{
		updateRowData(lastRange.getStart(), codes);
	}

	/**
	 * @return the codes
	 */
	public List<UICode> getCodes() {
		return codes;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	@Override
	public HandlerRegistration addGroupsChangedHandler(GroupsChangedHandler handler) {
		return handlerManager.addHandler(GroupsChangedEvent.TYPE, handler);
	}

}
