/**
 * 
 */
package org.cotrix.web.common.client.widgets.group;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.cotrix.web.common.client.util.CachedDataProvider;
import org.cotrix.web.common.client.util.DataUpdatedEvent;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.HasData;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class GroupedDataProvider<T> extends AbstractDataProvider<Group<T>> {
	
	public interface Grouper<T> {
		public String getGroupName(T codelistInfo);
	}
	
	private CachedDataProvider<T> dataProvider;
	private Grouper<T> grouper;
	private Map<String, Group<T>> lastGroups;

	public GroupedDataProvider(CachedDataProvider<T> dataProvider,
			Grouper<T> grouper) {
		this.dataProvider = dataProvider;
		this.grouper = grouper;
		
		dataProvider.addDataUpdatedHandler(new DataUpdatedEvent.DataUpdatedHandler() {
			
			@Override
			public void onDataUpdated(DataUpdatedEvent event) {
				Log.trace("onDataUpdated event: "+event);
				refreshData(0, !event.isRefresh());
			}
		});
	}

	public void setGrouper(Grouper<T> grouper) {
		this.grouper = grouper;
	}

	@Override
	protected void onRangeChanged(HasData<Group<T>> display) {
		Log.trace("onRangeChanged display: "+display);
		refreshData(display.getVisibleRange().getStart(), false);
	}
	
	private void refreshData(int start, boolean dataChanged) {
		Log.trace("refreshData start: "+start+" dataChanged: "+dataChanged);
		//we update the groups only if data is changed in sub provider
		if (dataChanged || lastGroups == null) updateGroups(start);
		else refreshSubItem();
	}
	
	private void refreshSubItem() {
		for (Group<T> group:lastGroups.values()) group.getItems().refresh();
	}
	
	private void updateGroups(int start) {
		List<T> items = dataProvider.getCache();
		if (items == null) return;
		Map<String, Group<T>> groups = createGroups(items);
		
		//we force groups visualization update only if the groups are changed
		boolean groupsChanged = lastGroups == null || !lastGroups.keySet().equals(groups.keySet());
		Log.trace("groupsChanged: "+groupsChanged);
		if (lastGroups!=null) Log.trace("lastGroups.keySet(): "+lastGroups.keySet());
		Log.trace("groups.keySet(): "+groups.keySet());
		
		if (groupsChanged) {
			updateRowCount(groups.size(), true);
			updateRowData(start, getValues(groups));
			lastGroups = groups;
		} else {
			for (Group<T> group:lastGroups.values()) {
				group.getItems().setList(groups.get(group.getName()).getItems().getList());
			}
		}
			
	}
	
	private List<Group<T>> getValues(Map<String, Group<T>> groupsIndex) {
		List<Group<T>> groups = new ArrayList<Group<T>>();
		for (String key:groupsIndex.keySet()) groups.add(groupsIndex.get(key));
		return groups;
	}
	
	private Map<String, Group<T>> createGroups(List<T> items) {
		Map<String, Group<T>> groupsIndex = new LinkedHashMap<String, Group<T>>();
		
		for (T item:items) {
			String groupName = grouper.getGroupName(item);
			
			Group<T> group = groupsIndex.get(groupName);
			if (group == null) {
				group = new Group<T>(groupName);
				groupsIndex.put(groupName, group);
			}
			group.addItem(item);

		}
		
		return groupsIndex;
	}
}
