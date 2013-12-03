package org.cotrix.domain.po;

import org.cotrix.common.Utils;
import org.cotrix.domain.trait.Versioned;
import org.cotrix.domain.version.Version;


/**
 * Partial implementation of initialisation parameters for {@link VersionedObject}s.
 * 
 * @author Fabio Simeoni
 *
 */
public abstract class VersionedPO<T extends Versioned.Abstract<T>> extends NamedPO<T> {

	private Version version;

	public VersionedPO(String id) {
		super(id);
	}
	
	/**
	 * Returns the version parameter.
	 * @return the parameter
	 */
	public Version version() {
		return version;
	}

	/**
	 * Sets the version parameter.
	 * @param version the parameter
	 */
	public void version(Version version) {
		Utils.notNull("version",version);
		this.version = version;
	}

}
