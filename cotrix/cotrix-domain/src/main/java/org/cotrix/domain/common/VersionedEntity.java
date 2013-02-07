package org.cotrix.domain.common;

import static org.cotrix.domain.utils.Utils.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Attributed;
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
	
	public T copy(String version) {
		
		notNull("version",version);
		
		Version newVersion = this.version.copy(version);
		
		return copy(newVersion);
	}
	
	public T copy() {
		return copy(version);
	}
	
	protected abstract T copy(Version version);
	
	
	

	
}
