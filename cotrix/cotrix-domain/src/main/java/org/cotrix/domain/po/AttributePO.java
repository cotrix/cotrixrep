package org.cotrix.domain.po;

import static org.cotrix.domain.utils.Utils.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.utils.Constants;
import org.cotrix.domain.utils.Utils;

/**
 * Initialisation parameters for {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public final class AttributePO extends EntityPO {

	private QName name;
	private QName type = Constants.DEFAULT_TYPE;
	private String value;
	private String language;
	
	/**
	 * Creates an instance with an identifier.
	 * @param id the identifier
	 */
	public AttributePO(String id) {
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
	public void setName(QName name) {
		
		Utils.valid(name);
		
		this.name = name;
	}	
	
	public QName type() {
		return type;
	}
	public String value() {
		return value;
	}
	
	public void setType(QName type) {
		valid("type",type);
		this.type = type;
	}
	
	public void setValue(String value) {
		
		valid("value",value);
		
		this.value = value;
	}

	public String language() {
		
		return language;
	}

	public void setLanguage(String language) {
		
		valid("language",language);
		
		this.language = language;
	}
}
