package org.cotrix.domain.versions;

import org.cotrix.domain.traits.Versioned;

/**
 * A {@link Versioned} that can serve as a version.
 * @author Fabio Simeoni
 *
 */
public interface Version extends Versioned<Version> {}
