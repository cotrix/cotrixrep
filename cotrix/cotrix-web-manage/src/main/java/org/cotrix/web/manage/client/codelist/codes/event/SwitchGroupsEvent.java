package org.cotrix.web.manage.client.codelist.codes.event;

import java.util.List;

import org.cotrix.web.manage.shared.Group;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SwitchGroupsEvent extends GenericEvent {

	private List<Group> groups;

	public SwitchGroupsEvent(List<Group> groups) {
		this.groups = groups;
	}

	public List<Group> getGroups() {
		return groups;
	}
}
