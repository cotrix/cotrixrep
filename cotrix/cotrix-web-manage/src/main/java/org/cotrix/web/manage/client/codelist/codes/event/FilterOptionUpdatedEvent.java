package org.cotrix.web.manage.client.codelist.codes.event;

import java.util.List;

import org.cotrix.web.manage.shared.filter.FilterOption;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FilterOptionUpdatedEvent extends GenericEvent {

	private List<FilterOption> filterOptions;

	public FilterOptionUpdatedEvent(List<FilterOption> filterOptions) {
		this.filterOptions = filterOptions;
	}

	public List<FilterOption> getFilterOptions() {
		return filterOptions;
	}

}
