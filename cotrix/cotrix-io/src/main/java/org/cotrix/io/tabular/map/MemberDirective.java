package org.cotrix.io.tabular.map;



import javax.xml.namespace.QName;

import org.cotrix.domain.trait.Defined;
import org.cotrix.domain.trait.Named;

/**
 * Directives to map codelist attributes onto table columns.
 * 
 * @author Fabio Simeoni
 * 
 */
public class MemberDirective {

	private final Named def;
	private QName columnName;
	
	public static <T extends Named> MemberDirective map(Defined<T> attribute) {
		return map(attribute.definition());
	}
	
	public static MemberDirective map(Named def) {
		return new MemberDirective(def);
	}
	
	
	/**
	 * Creates an instance for a given attribute template.
	 * @param template the template
	 */
	public MemberDirective(Named def) {
		this.def = def;
	}
	
	
	/**
	 * Returns the definition for this directives.
	 * @return the template
	 */
	public Named definition() {
		return def;
	}
	
	
	/**
	 * Returns the name of the target column.
	 * <p>
	 * By default, this is the name in the underlying definition.
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
