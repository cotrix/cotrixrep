package org.cotrix.domain.po;

import org.cotrix.common.Utils;
import org.cotrix.domain.version.SimpleVersion;
import org.cotrix.domain.version.Version;


/**
 * Partial implementation of initialisation parameters for {@link VersionedObject}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class VersionedPO extends NamedPO {

	private Version version = new SimpleVersion();

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
	public void setVersion(Version version) {
		Utils.notNull("version",version);
		this.version = version;
	}

}
