package org.cotrix.io.tabular.map;



import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;

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
	
	private MappingMode mode = MappingMode.LOG;
	
	
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
	 * @param column the column
	 */
	public AttributeDirectives(Attribute template) {
		this.template = template;
	}
	/**
	 * Returns the template template for these directives.
	 * @return the column
	 */
	public Attribute template() {
		return template;
	}
	
	
	/**
	 * Returns the name of the target template
	 * <p>
	 * By default, this is the name of the column.
	 * 
	 * @return the name of the template
	 */
	public QName columnName() {
		return columnName==null?template.name():columnName;
	}
	
	
	/**
	 * Sets the name of the target template
	 * 
	 * @param the name
	 * @return these directives
	 */
	public AttributeDirectives to(QName name) {
		this.columnName=name;
		return this;
	}
	
	/**
	 * Sets the name of the target template
	 * 
	 * @param the name
	 * @return these directives
	 */
	public AttributeDirectives to(String name) {
		return to(new QName(name));
	}
	
	/**
	 * Sets the mode of these directives.
	 * <p>
	 * By default, the mode is {@link MappingMode#LOG}.
	 * 
	 * @param mode the mode
	 * @return these directives
	 */
	public AttributeDirectives mode(MappingMode mode) {
		this.mode = mode;
		return this;
	}
	
	/**
	 * Returns the mode of these directives.
	 * @return the mode
	 */
	public MappingMode mode() {
		return mode;
	}
	
}
