package org.cotrix.web.manage.client.codelist.event;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CreateNewVersionEvent extends GenericEvent {
	
	private String codelistId;
	private String newVersion;

	public CreateNewVersionEvent(String codelistId, String newVersion) {
		this.codelistId = codelistId;
		this.newVersion = newVersion;
	}

	public String getCodelistId() {
		return codelistId;
	}

	public String getNewVersion() {
		return newVersion;
	}
}
