package org.cotrix.web.ingest.client.event;

import org.cotrix.web.common.shared.codelist.UIQName;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RepositoryDetailsRequestEvent extends GenericEvent {
	
	private UIQName repositoryId;

	public RepositoryDetailsRequestEvent(UIQName repositoryId) {
		this.repositoryId = repositoryId;
	}

	public UIQName getRepositoryId() {
		return repositoryId;
	}

}
