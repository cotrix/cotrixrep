package org.cotrix.domain.memory;

import org.cotrix.common.CommonUtils;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.Status;
import org.cotrix.domain.trait.Versioned;
import org.cotrix.domain.version.DefaultVersion;
import org.cotrix.domain.version.Version;


public abstract class MVersioned extends MAttributed implements Versioned.Bean, Identified.Bean, Attributed.Bean, Named.Bean {

	private Version version;

	public MVersioned() {
		version = new DefaultVersion();
	}
	
	public MVersioned(String id,Status status) {
		super(id,status);
	}
	
	public <T extends Attributed.Bean & Versioned.Bean & Named.Bean> MVersioned(T state) {
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
		if (!(obj instanceof Versioned.Bean))
			return false;
		Versioned.Bean other = (Versioned.Bean) obj;
		if (version == null) {
			if (other.version() != null)
				return false;
		} else if (!version.equals(other.version()))
			return false;
		return true;
	}

}
