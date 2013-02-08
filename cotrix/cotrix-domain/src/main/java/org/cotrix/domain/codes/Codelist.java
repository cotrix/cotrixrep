package org.cotrix.domain.codes;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Attributed;
import org.cotrix.domain.containers.Bag;
import org.cotrix.domain.containers.Group;
import org.cotrix.domain.entities.VersionedEntity;
import org.cotrix.domain.traits.Named;
import org.cotrix.domain.traits.Versioned;
import org.cotrix.domain.versions.SimpleVersion;
import org.cotrix.domain.versions.Version;

/**
 * A {@link Named}, {@link Attributed}, and {@link Versioned} list of {@link Code}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class Codelist extends VersionedEntity<Codelist> implements Versioned<Codelist> {

	private final Group<Code> codes;
	
	/**
	 * Creates an instance with a given name.
	 * @param name the name
	 */
	public Codelist(QName name) {
		this(name,new Group<Code>());
	}
	
	/**
	 * Creates an instance with a given name and given codes.
	 * @param name the name
	 * @param codes the codes
	 */
	public Codelist(QName name, Group<Code> codes) {
		this(name,codes, new Bag<Attribute>());
	}
	
	/**
	 * Creates an instance with a given name, codes, and attributes.
	 * @param name the name
	 * @param codes the codes
	 * @param attributes the attributes
	 */
	public Codelist(QName name, Group<Code> codes,Bag<Attribute> attributes) {
		this(name,codes,attributes,new SimpleVersion());
	}
	
	/**
	 * Creates an instance with given name, codes, attributes, and version.
	 * @param name the name
	 * @param codes the codes
	 * @param attributes the attributes
	 * @param version the version
	 */
	public Codelist(QName name, Group<Code> codes,Bag<Attribute> attributes,Version version) {
		super(name,attributes,version);
		this.codes=codes;
	}
	
	/**
	 * Returns the codes of this list.
	 * @return the codes
	 */
	public Group<Code> codes() {
		return codes;
	}
	
	@Override
	protected Codelist copy(Version version) throws IllegalArgumentException,IllegalStateException {
		return new Codelist(name(),codes().copy(),attributes().copy(),version);
	}
	

	@Override
	public String toString() {
		return "Codelist [name=" + name() + ", codes=" + codes + ", attributes=" + attributes() + ", version=" + version() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((codes == null) ? 0 : codes.hashCode());
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
		Codelist other = (Codelist) obj;
		if (codes == null) {
			if (other.codes != null)
				return false;
		} else if (!codes.equals(other.codes))
			return false;
		return true;
	}

	
	
}
