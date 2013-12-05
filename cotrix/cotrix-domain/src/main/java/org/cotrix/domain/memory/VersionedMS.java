package org.cotrix.domain.memory;

import org.cotrix.common.Utils;
import org.cotrix.domain.trait.Status;
import org.cotrix.domain.trait.Versioned;
import org.cotrix.domain.version.DefaultVersion;
import org.cotrix.domain.version.Version;


public abstract class VersionedMS extends NamedMS implements Versioned.State {

	private Version version;

	public VersionedMS() {
		version = new DefaultVersion();
	}
	
	public VersionedMS(String id,Status status) {
		super(id,status);
	}
	
	public Version version() {
		return version;
	}
	
	@Override
	public void version(Version version) {
		
		Utils.notNull("version",version);
		
		this.version=version;
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
