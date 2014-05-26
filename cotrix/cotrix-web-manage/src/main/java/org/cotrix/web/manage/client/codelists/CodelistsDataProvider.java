/**
 * 
 */
package org.cotrix.web.manage.client.codelists;

import java.util.List;

import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.util.FilteredCachedDataProvider;
import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.shared.CodelistGroup;
import org.cotrix.web.manage.shared.CodelistGroup.Version;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CodelistsDataProvider extends FilteredCachedDataProvider<CodelistGroup> {
	
	@Inject
	protected ManageServiceAsync managerService;

	@Override
	protected void onRangeChanged(HasData<CodelistGroup> display) {
		final Range range = display.getVisibleRange();
		onRangeChanged(range);
	}
	
	public void loadData()
	{
		onRangeChanged((Range)null);
	}
	
	public void addCodelistGroup(CodelistGroup newGroup)
	{
		Log.trace("addCodelistGroup newGroup: "+newGroup);
		CodelistGroup oldGroup = findGroupInCache(newGroup);
		Log.trace("oldGroup: "+oldGroup);
		
		if (oldGroup!=null) oldGroup.addVersions(newGroup.getVersions());
		else cache.add(newGroup);
		
		Log.trace("refreshing cache: "+cache);
		refresh();
	}
	
	public void removeCodelistGroup(CodelistGroup groupToRemove)
	{
		Log.trace("removeCodelistGroup groupToRemove: "+groupToRemove);
		CodelistGroup oldGroup = findGroupInCache(groupToRemove);
		Log.trace("oldGroup: "+oldGroup);
		
		if (oldGroup == null) return;
		
		for (Version version:groupToRemove.getVersions()) oldGroup.removeVersion(version);
		if (oldGroup.getVersions().isEmpty()) cache.remove(oldGroup);
		
		Log.trace("oldGroup after update: "+oldGroup);
		
		Log.trace("refreshing cache: "+cache);
		updateData(cache, new Range(0, cache.size()), cache.size());
	}
	
	private CodelistGroup findGroupInCache(CodelistGroup groupToFind) {
		for (CodelistGroup group:cache) {
			if (group.equals(groupToFind)) return group;
		}
		return null;
	}
	
	public boolean containsVersion(Version version) {
		CodelistGroup group = findGroupInCache(version.getParent());
		if (group == null) return false;
		return group.getVersions().contains(version);
	}

	@Override
	protected void onRangeChanged(final Range range) {
		managerService.getCodelistsGrouped(new ManagedFailureCallback<DataWindow<CodelistGroup>>() {

			@Override
			public void onSuccess(DataWindow<CodelistGroup> result) {
				List<CodelistGroup> groups = result.getData();
				Log.trace("loaded "+groups.size()+" codelists");
				if (range == null) updateData(groups, new Range(0, result.getTotalSize()), result.getTotalSize());
				else updateData(groups, range, result.getTotalSize());
					
			}
		});		
	}
	

}
