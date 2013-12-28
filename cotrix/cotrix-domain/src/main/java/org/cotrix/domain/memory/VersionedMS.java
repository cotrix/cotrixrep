package org.cotrix.domain.memory;

import org.cotrix.common.Utils;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.Status;
import org.cotrix.domain.trait.Versioned;
import org.cotrix.domain.version.DefaultVersion;
import org.cotrix.domain.version.Version;


public abstract class VersionedMS extends NamedMS implements Versioned.State, Identified.State, Attributed.State, Named.State {

	private Version version;

	public VersionedMS() {
		version = new DefaultVersion();
	}
	
	public VersionedMS(String id,Status status) {
		super(id,status);
	}
	
	public <T extends Attributed.State & Versioned.State & Named.State> VersionedMS(T state) {
		super(state);
		version(state.version());
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
