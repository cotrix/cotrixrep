package org.cotrix.io.tabular.map;



import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;

/**
 * Directives to map codelist attributes onto table columns.
 * 
 * 
 * @author Fabio Simeoni
 * 
 */
public class AttributeDirectives {

	private final Attribute template;
	private QName columnName;
	
	/**
	 * Creates mapping directives for a given attribute template.
	 * @param template the template
	 * @return the directives
	 */
	public static AttributeDirectives map(Attribute template) {
		return new AttributeDirectives(template);
	}
	
	
	/**
	 * Creates an instance for a given attribute template.
	 * @param template the template
	 */
	public AttributeDirectives(Attribute template) {
		this.template = template;
	}
	
	
	/**
	 * Returns the attribute template for these directives.
	 * @return the template
	 */
	public Attribute template() {
		return template;
	}
	
	
	/**
	 * Returns the name of the target column.
	 * <p>
	 * By default, this is the name in the attribute template.
	 * 
	 * @return the name of the target column
	 */
	public QName columnName() {
		return columnName==null?template.qname():columnName;
	}
	
	
	/**
	 * Sets the name of the target column.
	 * 
	 * @param name the name
	 * @return these directives
	 */
	public AttributeDirectives to(QName name) {
		this.columnName=name;
		return this;
	}
	
	/**
	 * Sets the name of the target column
	 * 
	 * @param name the name
	 * @return these directives
	 */
	public AttributeDirectives to(String name) {
		return to(new QName(name));
	}
	
}
