package org.cotrix.domain.po;

import static org.cotrix.domain.utils.Utils.*;

import org.cotrix.domain.version.SimpleVersion;
import org.cotrix.domain.version.Version;


/**
 * A partial implementation of parameter objects for {@link VersionedObject}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class VersionedPO extends AttributedPO {

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
		notNull("version",version);
		this.version = version;
	}
	
	
	
}
