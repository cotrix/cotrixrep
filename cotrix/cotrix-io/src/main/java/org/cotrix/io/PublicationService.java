package org.cotrix.io;

import javax.xml.namespace.QName;

import org.cotrix.domain.Codelist;
import org.cotrix.io.publish.PublicationDirectives;
import org.virtualrepository.RepositoryService;

/**
 * Publish {@link Codelist}s through one of a number of supported <em>publication channels</em>.
 * <p>
 * PublicationDirectives channels are represented by {@link RepositoryService}s.
 * 
 * @author Fabio Simeoni
 *
 */
public interface PublicationService {

	/**
	 * Publishes a given {@link Codelist} through a given channel according to given {@link PublicationDirectives}.
	 * @param codelist the codelist
	 * @param mockDirectives the mockDirectives
	 * @param channel the name of the channel
	 * 
	 * @throws IllegalArgumentException if the state of the codelist is such that it cannot published
	 * @throws IllegalStateException if a channel with the given name is unknown to this publisher
	 * @throws RuntimeException if publishing the given codelist through the given channel fails
	 */
	void publish(Codelist codelist, PublicationDirectives directives, QName channel);
}
