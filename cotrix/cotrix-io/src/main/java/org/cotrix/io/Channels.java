package org.cotrix.io;

import java.util.Collection;

import javax.inject.Inject;

import org.virtualrepository.AssetType;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.sdmx.SdmxCodelist;

/**
 * A collection of import and publication channels. 
 * 
 * @author Fabio Simeoni
 *
 */
public class Channels {

	/**
	 * The types supported for publication.
	 */
	public static final AssetType[] publicationTypes = {SdmxCodelist.type};
	
	/**
	 * The types supported for import.
	 */
	public static final AssetType[] importTypes = {SdmxCodelist.type};
	
	private final VirtualRepository repository;
	
	/**
	 * Creates an instance over a {@link VirtualRepository}.
	 * @param repository the repository
	 */
	@Inject
	public Channels(VirtualRepository repository) {
		this.repository=repository;
	}
	
	/**
	 * Returns the publication channels.
	 * @return the publication channels
	 */
	Collection<RepositoryService> publicationChannels() {
		return repository.sinks(publicationTypes);
	}
	
	/**
	 * Adds a channel, overwriting any that has the same name.
	 * @param channel the channel
	 */
	public void addChannel(RepositoryService channel) {
		repository.services().add(channel);
	}
}
