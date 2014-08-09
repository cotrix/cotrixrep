package org.cotrix.domain.memory;

import org.cotrix.common.CommonUtils;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.Status;
import org.cotrix.domain.trait.Versioned;
import org.cotrix.domain.version.DefaultVersion;
import org.cotrix.domain.version.Version;


public abstract class VersionedMS extends NamedMS implements Versioned.State, Identified.Bean, Attributed.Bean, Named.Bean {

	private Version version;

	public VersionedMS() {
		version = new DefaultVersion();
	}
	
	public VersionedMS(String id,Status status) {
		super(id,status);
	}
	
	public <T extends Attributed.Bean & Versioned.State & Named.Bean> VersionedMS(T state) {
		super(state);
		version(state.version());
	}
	
	public Version version() {
		return version;
	}
	
	@Override
	public void version(Version version) {
		
		CommonUtils.notNull("version",version);
		
		this.version=version;
	}

	@Override
	@SuppressWarnings("all")
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Versioned.State))
			return false;
		Versioned.State other = (Versioned.State) obj;
		if (version == null) {
			if (other.version() != null)
				return false;
		} else if (!version.equals(other.version()))
			return false;
		return true;
	}

}
