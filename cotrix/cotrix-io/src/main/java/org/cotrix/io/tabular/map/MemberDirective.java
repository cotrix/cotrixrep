package org.cotrix.io.tabular.map;



import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Definition;

/**
 * Directives to map codelist attributes onto table columns.
 * 
 * @author Fabio Simeoni
 * 
 */
public class MemberDirective {

	private final Definition def;
	private QName columnName;
	
	public static MemberDirective map(Attribute attribute) {
		return map(attribute.definition());
	}
	
	public static MemberDirective map(Definition def) {
		return new MemberDirective(def);
	}
	
	
	/**
	 * Creates an instance for a given attribute template.
	 * @param template the template
	 */
	public MemberDirective(Definition def) {
		this.def = def;
	}
	
	
	/**
	 * Returns the attribute template for these directives.
	 * @return the template
	 */
	public Definition definition() {
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
	public MemberDirective to(QName name) {
		this.columnName=name;
		return this;
	}
	
	/**
	 * Sets the name of the target column
	 * 
	 * @param name the name
	 * @return these directives
	 */
	public MemberDirective to(String name) {
		return to(new QName(name));
	}
	
}
