package org.cotrix.web.ingest.client.event;

import org.cotrix.web.ingest.shared.CodeListType;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListTypeUpdatedEvent extends GenericEvent {

	private CodeListType codeListType;
	
	public CodeListTypeUpdatedEvent(CodeListType codeListType) {
		this.codeListType = codeListType;
	}

	/**
	 * @return the codeListType
	 */
	public CodeListType getCodeListType() {
		return codeListType;
	}
}
