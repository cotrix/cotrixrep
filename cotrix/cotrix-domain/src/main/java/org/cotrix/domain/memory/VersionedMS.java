package org.cotrix.domain.memory;

import org.cotrix.common.Utils;
import org.cotrix.domain.version.Version;


/**
 * Partial implementation of initialisation parameters for {@link VersionedObject}s.
 * 
 * @author Fabio Simeoni
 *
 */
public abstract class VersionedMS extends NamedMS {

	private Version version;

	public VersionedMS(String id) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	@SuppressWarnings("all")
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof VersionedMS))
			return false;
		VersionedMS other = (VersionedMS) obj;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
	
	

}
