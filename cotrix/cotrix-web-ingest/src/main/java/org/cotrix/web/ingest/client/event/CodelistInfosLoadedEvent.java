package org.cotrix.web.ingest.client.event;

import java.util.List;

import com.google.web.bindery.event.shared.binder.GenericEvent;

import org.cotrix.web.ingest.shared.CodelistInfo;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistInfosLoadedEvent extends GenericEvent {

	private List<CodelistInfo> codelistInfos;

	public CodelistInfosLoadedEvent(List<CodelistInfo> codelistInfos) {
		this.codelistInfos = codelistInfos;
	}

	public List<CodelistInfo> getCodelistInfos() {
		return codelistInfos;
	}
}
