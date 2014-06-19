/**
 * 
 */
package org.cotrix.web.manage.client.codelists;

import java.util.Iterator;
import java.util.List;

import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.util.FilteredCachedDataProvider;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.codelist.CodelistNewStateEvent;
import org.cotrix.web.manage.client.event.CodelistCreatedEvent;
import org.cotrix.web.manage.client.event.CodelistRemovedEvent;
import org.cotrix.web.manage.client.event.ManagerBus;
import org.cotrix.web.manage.shared.UICodelistInfo;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CodelistsDataProvider extends FilteredCachedDataProvider<UICodelistInfo> {
	
	interface CodelistsDataProviderEventBinder extends EventBinder<CodelistsDataProvider> {}
	
	@Inject
	protected ManageServiceAsync managerService;
	
	@Inject
	private void bind(CodelistsDataProviderEventBinder binder, @ManagerBus EventBus managerBus) {
		binder.bindEventHandlers(this, managerBus);
	}
	
	@EventHandler
	protected void onStateUpdate(CodelistNewStateEvent event) {
		UICodelist codelist = event.getCodelist();
		
		UICodelistInfo codelistInfo = findCodelistInCache(codelist);
		if (codelistInfo != null) {
			codelistInfo.setState(event.getState());
			refresh();
		}
	}
	
	@EventHandler
	void onCodelistCreated(CodelistCreatedEvent event) {
		addCodelist(event.getCodelistInfo());
	}
	
	@EventHandler
	void onCodelistRemoved(CodelistRemovedEvent event) {
		removeCodelist(event.getCodelist());
	}
	
	private UICodelistInfo findCodelistInCache(UICodelist codelist) {
		for (UICodelistInfo codelistInfo:cache) if (codelistInfo.getId().equals(codelist.getId())) return codelistInfo;
		return null;
	}
	
	public void loadData()
	{
		onRangeChanged((Range)null);
	}
	
	public void addCodelist(UICodelistInfo codelist)
	{
		Log.trace("addCodelist codelist: "+codelist);
		cache.add(codelist);
		
		Log.trace("refreshing cache: "+cache);
		updateData(cache, new Range(0, cache.size()), cache.size());
	}
	
	public void removeCodelist(UICodelist codelist)
	{
		Log.trace("removeCodelist codelist: "+codelist);
		
		Iterator<UICodelistInfo> iterator = cache.iterator();
		while(iterator.hasNext()) {
			if (iterator.next().getId().equals(codelist.getId())) iterator.remove();
		}
		
		Log.trace("refreshing cache: "+cache);
		updateData(cache, new Range(0, cache.size()), cache.size());
	}

	@Override
	protected void onRangeChanged(final Range range) {
		managerService.getCodelistsInfos(new ManagedFailureCallback<List<UICodelistInfo>>() {

			@Override
			public void onSuccess(List<UICodelistInfo> result) {
				Log.trace("loaded "+result.size()+" codelists");
				//Collections.sort(groups, COMPARATOR);
				if (range == null) updateData(result, new Range(0, result.size()), result.size());
				else updateData(result, range, result.size());
					
			}
		});		
	}	

}
