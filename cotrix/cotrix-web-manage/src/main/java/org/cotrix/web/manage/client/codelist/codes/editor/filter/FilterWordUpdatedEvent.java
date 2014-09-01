/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.editor.filter;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FilterWordUpdatedEvent extends GenericEvent {
	
	private String word;

	public FilterWordUpdatedEvent(String word) {
		this.word = word;
	}

	public String getWord() {
		return word;
	}

}
