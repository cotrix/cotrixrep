package org.cotrix.domain.version;

import org.cotrix.domain.trait.Versioned;

/**
 * A {@link Versioned} that can serve as a version.
 * @author Fabio Simeoni
 *
 */
public interface Version {
	
	String value();
	
	Version bumpTo(String version);
}
