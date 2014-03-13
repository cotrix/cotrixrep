package org.cotrix.web.ingest.client.event;

import org.cotrix.web.ingest.shared.AssetInfo;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListSelectedEvent extends GenericEvent {
	
	private AssetInfo selectedCodelist;

	public CodeListSelectedEvent(AssetInfo selectedCodelist) {
		this.selectedCodelist = selectedCodelist;
	}

	/**
	 * @return the selectedCodelist
	 */
	public AssetInfo getSelectedCodelist() {
		return selectedCodelist;
	}
}
