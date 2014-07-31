/**
 * 
 */
package org.cotrix.web.manage.client.codelist;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistTaskCompleteEvent extends GenericEvent {
	
	public enum Task {VALIDATION, CHANGELOG_GENERATION};
	
	private Task task;

	public CodelistTaskCompleteEvent(Task task) {
		this.task = task;
	}

	public Task getTask() {
		return task;
	}

}
