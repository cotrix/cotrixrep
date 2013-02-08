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
 * A {@link Named}, {@link Attributed}, and {@link Versioned} bag of {@link Codelist}s.
 * @author Fabio Simeoni
 *
 */
public class Codebag extends VersionedEntity<Codebag> implements Versioned<Codebag> {

	private final Group<Codelist> lists;
	
	/**
	 * Creates an instance with a given name.
	 * @param name
	 */
	public Codebag(QName name) {
		this(name, new Group<Codelist>());
	}
	
	/**
	 * Creates an instance with a given name and given code lists.
	 * @param name the name
	 * @param attributes the code lists
	 */
	public Codebag(QName name,Group<Codelist> lists) {
		this(name,lists,new Bag<Attribute>());
	}
	
	/**
	 * Creates an instance with a given name, code lists, and attributes.
	 * @param name the name
	 * @param lists the code lists
	 * @param attributes the attributes
	 * 
	 */
	public Codebag(QName name,Group<Codelist> lists,Bag<Attribute> attributes) {
		this(name,lists,attributes, new SimpleVersion());
	}
	
	/**
	 * Creates an instance with a given name, code lists, and attributes.
	 * @param name the name
	 * @param lists the code lists
	 * @param attributes the attributes
	 * 
	 */
	public Codebag(QName name,Group<Codelist> lists,Bag<Attribute> attributes,Version version) {
		super(name,attributes,version);
		this.lists=lists;
	}
	
	/**
	 * Returns the code lists in this bag.
	 * @return the lists.
	 */
	public Group<Codelist> lists() {
		return lists;
	}
	
	@Override
	public Codebag copy(Version version) {
		return new Codebag(name(),lists().copy(),attributes().copy(),version);
	}

	@Override
	public String toString() {
		return "CodeBag [name=" + name() + ", lists=" + lists + ", attributes=" + attributes() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((lists == null) ? 0 : lists.hashCode());
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
		Codebag other = (Codebag) obj;
		if (lists == null) {
			if (other.lists != null)
				return false;
		} else if (!lists.equals(other.lists))
			return false;
		return true;
	}

	
	
}
