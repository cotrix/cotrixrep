package org.cotrix.domain.pos;

import static org.cotrix.domain.utils.Utils.*;

import org.cotrix.domain.common.VersionedObject;
import org.cotrix.domain.versions.SimpleVersion;
import org.cotrix.domain.versions.Version;


/**
 * A partial implementation of parameter objects for {@link VersionedObject}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class VersionedPO extends AttributedPO {

	private Version version = new SimpleVersion();

	protected VersionedPO(String id) {
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
	public void setVersion(Version version) {
		notNull("version",version);
		this.version = version;
	}
	
	
	
}
