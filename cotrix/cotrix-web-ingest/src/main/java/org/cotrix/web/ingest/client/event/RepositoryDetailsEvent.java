package org.cotrix.web.ingest.client.event;

import org.cotrix.web.common.shared.codelist.RepositoryDetails;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RepositoryDetailsEvent extends GenericEvent {
	
	private RepositoryDetails repositoryDetails;

	public RepositoryDetailsEvent(RepositoryDetails repositoryDetails) {
		this.repositoryDetails = repositoryDetails;
	}

	public RepositoryDetails getRepositoryDetails() {
		return repositoryDetails;
	}

}
