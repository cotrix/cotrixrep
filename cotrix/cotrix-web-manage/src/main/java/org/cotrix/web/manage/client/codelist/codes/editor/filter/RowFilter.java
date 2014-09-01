/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.editor.filter;

import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.manage.client.di.CodelistBus;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RowFilter {
	
	interface RowFilterEventBinder extends EventBinder<RowFilter> {}
	
	private String word;
	
	@Inject
	void bind(@CodelistBus EventBus bus, RowFilterEventBinder binder) {
		binder.bindEventHandlers(this, bus);
	}
	
	public boolean isActive() {
		return word != null && !word.isEmpty();
	}
	
	public boolean accept(UICode code) {
		if (word == null) return false;
		
		if (contains(code.getName(), word)) return true;
		
		for (UIAttribute attribute:code.getAttributes()) {
			if (contains(attribute.getName(), word)) return true;
			if (contains(attribute.getValue(), word)) return true;
		}
		
		for (UILink link:code.getLinks()) {
			if (contains(link.getDefinitionName(), word)) return true;
			if (contains(link.getValue(), word)) return true;
		}
		
		return false;
	}
	
	@EventHandler
	void onWordUpdate(FilterWordUpdatedEvent event) {
		this.word = event.getWord()!=null && event.getWord().isEmpty()?null:event.getWord().toLowerCase();
	}
	
	private boolean contains(UIQName name, String value) {
		return name!=null && name.getLocalPart()!=null && name.getLocalPart().toLowerCase().contains(value);
	}
	
	private boolean contains(String name, String value) {
		return name!=null && name.toLowerCase().contains(value);
	}

}
