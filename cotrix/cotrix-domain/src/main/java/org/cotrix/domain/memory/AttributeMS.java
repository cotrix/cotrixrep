package org.cotrix.domain.memory;

import static org.cotrix.common.Utils.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.utils.Constants;

/**
 * Initialisation parameters for {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public final class AttributeMS extends IdentifiedMS implements Attribute.State {

	private QName name;
	private QName type = Constants.DEFAULT_TYPE;
	private String value;
	private String language;
	
	/**
	 * Creates an instance with an identifier.
	 * @param id the identifier
	 */
	public AttributeMS(String id) {
		super(id);
	}
	
	
	/** Returns the name parameter.
	 * @return the name parameter
	 */
	public QName name() {
		return name;
	}
	
	/**
	 * Sets the name parameter
	 * @param name the name parameter
	 */
	public void name(QName name) {
		
		valid("name",name);
		
		this.name = name;
	}	
	
	public QName type() {
		return type;
	}
	public String value() {
		return value;
	}
	
	public void type(QName type) {
		this.type = type;
	}
	
	public void value(String value) {
		
		this.value = value;
	}

	public String language() {
		
		return language;
	}

	public void language(String language) {
		
		this.language = language;
	}
	
	@Override
	public Private entity() {
		return new Attribute.Private(this);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof AttributeMS))
			return false;
		AttributeMS other = (AttributeMS) obj;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "AttributeMS [name=" + name + ", type=" + type + ", value=" + value + ", language=" + language
				+ ", id()=" + id() + ", status()=" + status() + "]";
	}



	
	
	
	
}
