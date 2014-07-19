package org.cotrix.io.tabular.map;


import javax.xml.namespace.QName;

import org.cotrix.domain.trait.Defined;
import org.cotrix.domain.trait.Definition;

/**
 * Directives to map definitions onto table columns.
 * 
 * @author Fabio Simeoni
 * 
 */
public class MemberDirective<T extends Definition> {

	//------ factory methods
	
	public static <T extends Definition> MemberDirective<T> map(Defined<T> defined) { //test convenience
		return map(defined.definition());
	}
	
	/**
	 * Creates a directive from a definition.
	 * @param definition the definition
	 * @return the directive
	 */
	public static <T extends Definition> MemberDirective<T> map(T definition) {
		return new MemberDirective<T>(definition);
	}
	
	//------------------------------------------------
	
	
	
	private final T def;
	private QName columnName;
	
	
	private MemberDirective(T definition) {
		this.def = definition;
	}
	

	public T definition() {
		return def;
	}
	
	
	public QName column() {
		return columnName==null?def.qname():columnName;
	}
	
	
	/**
	 * Sets the name of the column.
	 * 
	 * @param name the name
	 * @return these directives
	 */
	public MemberDirective<T> to(QName name) {
		this.columnName=name;
		return this;
	}
	
	/**
	 * Sets the name of the column
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
