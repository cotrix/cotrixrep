package org.cotrix.io;

import java.io.OutputStream;

/**
 * Serialises objects to streams according to directives.
 * 
 * @author Fabio Simeoni
 * 
 */
public interface SerialisationService {

	/**
	 * Serialisation directives.
	 * 
	 * @author Fabio Simeoni
	 * 
	 * @param <T> the type of serialised objects
	 */
	// implementation will look up dynamically, but interface provides safety
	public interface SerialisationDirectives<T> {
	}

	/**
	 * Serialises a given object object to a given stream according to given directives.
	 * 
	 * @param object the source
	 * @param stream the stream
	 * @param directives the directives
	 * 
	 * @param <T> the type of serialised objects
	 */
	<T> void serialise(T object, OutputStream stream, SerialisationDirectives<T> directives);
}
