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
		if (!(obj instanceof VersionedPO))
			return false;
		VersionedPO other = (VersionedPO) obj;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
	
	

}
