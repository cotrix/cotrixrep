package org.cotrix.domain.codes;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Attributed;
import org.cotrix.domain.containers.Bag;
import org.cotrix.domain.entities.CoreEntity;
import org.cotrix.domain.traits.Copyable;
import org.cotrix.domain.traits.Named;


//immutable value-object
/**
 * A {@link Named}, {@link Attributed}, and {@link Copyable} code.
 * 
 * @author Fabio Simeoni
 *
 */
public class Code extends CoreEntity<Code> {

	/**
	 * Creates an instance with a given name.
	 * @param name
	 */
	public Code(QName name) {
		this(name,new Bag<Attribute>());
	}
	
	/**
	 * Creates an instance with a given name and attributes
	 * @param name the name
	 * @param attributes the attributes
	 */
	public Code(QName name,Bag<Attribute> attributes) {
		super(name,attributes);
	}
	
	public Code copy() {
		return new Code(name(),attributes().copy());
	}

	@Override
	public String toString() {
		return "[name=" + name() + ", attributes=" + attributes() + "]";
	}

	

	
}
