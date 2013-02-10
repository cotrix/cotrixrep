package org.cotrix.domain.entities;

import static org.cotrix.domain.utils.Utils.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Attributed;
import org.cotrix.domain.containers.Bag;
import org.cotrix.domain.traits.Copyable;
import org.cotrix.domain.traits.Named;
import org.cotrix.domain.traits.Versioned;
import org.cotrix.domain.versions.Version;

/**
 * Partial implementation of {@link Named}, {@link Attributed}, and {@link Copyable} domain objects.
 * 
 * @author Fabio Simeoni
 *
 */
public abstract class VersionedEntity<T extends VersionedEntity<T>> extends CoreEntity<T> implements Versioned<T> {

	private final Version version;

	/**
	 * Creates an instance with a given name, attributes, and version
	 * @param name the name
	 * @param attributes the attributes
	 * @param version the version
	 */
	public VersionedEntity(QName name,Bag<Attribute> attributes,Version version) {
		
		super(name,attributes);
		this.version=version;
	}
	
	public String version() {
		return version.version();
	}
	
	public T copyWithVersion(String version) {
		
		notNull("version",version);
		
		Version newVersion = this.version.copyWithVersion(version);
		
		return copy(newVersion);
	}
	
	public T copy() {
		return copy(version);
	}
	
	protected abstract T copy(Version version);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		VersionedEntity<?> other = (VersionedEntity<?>) obj;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
	
	
	

	
}
