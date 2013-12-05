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
import org.cotrix.web.share.client.error.ManagedFailureCallback;
import org.cotrix.web.share.client.util.CachedDataProvider;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.codelist.UICode;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistCodesProvider extends CachedDataProvider<UICode> implements HasGroupsChangedHandlers {
	
	protected HandlerManager handlerManager = new HandlerManager(this);
	
	@Inject
	protected ManagerServiceAsync managerService;
	
	@Inject @CodelistId
	protected String codelistId;
	

	@Override
	protected void onRangeChanged(final Range range) {
		managerService.getCodelistCodes(codelistId, range, new ManagedFailureCallback<DataWindow<UICode>>() {

			@Override
			public void onSuccess(DataWindow<UICode> result) {
					Log.trace("loaded "+result.getData().size()+" rows");
				checkGroups(result.getData());
				updateData(result.getData(), range, result.getTotalSize());
			}
		});
	}
	
	protected void checkGroups(List<UICode> rows)
	{
		Set<Group> groups = GroupFactory.getGroups(rows);
		handlerManager.fireEvent(new GroupsChangedEvent(groups));
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
