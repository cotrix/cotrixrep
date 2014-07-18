package org.cotrix.io.tabular.map;



import javax.xml.namespace.QName;

import org.cotrix.domain.trait.Defined;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;

/**
 * Directives to map codelist attributes onto table columns.
 * 
 * @author Fabio Simeoni
 * 
 */
public class MemberDirective<T extends Named & Identified> {

	private final T def;
	private QName columnName;
	
	
	public static <T extends Named & Identified> MemberDirective<T> map(Defined<T> defined) {
		return mapdef(defined.definition());
	}
	
	public static <T extends Named & Identified> MemberDirective<T> mapdef(T def) {
		return new MemberDirective<T>(def);
	}
	
	
	/**
	 * Creates an instance for a given definition.
	 * @param def the definition
	 */
	public MemberDirective(T def) {
		this.def = def;
	}
	
	
	/**
	 * Returns the definition for this directives.
	 * @return the definition
	 */
	public T definition() {
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
	public MemberDirective<T> to(QName name) {
		this.columnName=name;
		return this;
	}
	
	/**
	 * Sets the name of the target column
	 * 
	 * @param name the name
	 * @return these directives
	 */
	public MemberDirective<T> to(String name) {
		return to(new QName(name));
	}

	@Override
	public String toString() {
		return "Directive [def=" + def + ", columnName=" + columnName + "]";
	}

	
}
