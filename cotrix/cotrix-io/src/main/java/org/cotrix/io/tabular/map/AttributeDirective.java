package org.cotrix.io.tabular.map;



import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;

/**
 * Directives to map codelist attributes onto table columns.
 * 
 * @author Fabio Simeoni
 * 
 */
public class AttributeDirective {

	private final AttributeDefinition def;
	private QName columnName;
	
	public static AttributeDirective map(Attribute attribute) {
		return map(attribute.definition());
	}
	
	public static AttributeDirective map(AttributeDefinition def) {
		return new AttributeDirective(def);
	}
	
	
	/**
	 * Creates an instance for a given attribute template.
	 * @param template the template
	 */
	public AttributeDirective(AttributeDefinition def) {
		this.def = def;
	}
	
	
	/**
	 * Returns the attribute template for these directives.
	 * @return the template
	 */
	public AttributeDefinition definition() {
		return def;
	}
	
	
	/**
	 * Returns the name of the target column.
	 * <p>
	 * By default, this is the name in the underlying attribute definition.
	 * 
	 * @return the name of the target column
	 */
	public QName columnName() {
		return columnName==null?def.qname():columnName;
	}
	
	
	/**
	 * Sets the name of the target column.
	 * 
	 * @param name the name
	 * @return these directives
	 */
	public AttributeDirective to(QName name) {
		this.columnName=name;
		return this;
	}
	
	/**
	 * Sets the name of the target column
	 * 
	 * @param name the name
	 * @return these directives
	 */
	public AttributeDirective to(String name) {
		return to(new QName(name));
	}
	
}
