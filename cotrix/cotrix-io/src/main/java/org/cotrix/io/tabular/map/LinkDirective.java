package org.cotrix.io.tabular.map;



import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.CodelistLink;

/**
 * Directives to map codelist attributes onto table columns.
 * 
 * 
 * @author Fabio Simeoni
 * 
 */
public class LinkDirective {

	private final CodelistLink def;
	private QName columnName;
	
	
	public static LinkDirective map(CodelistLink def) {
		return new LinkDirective(def);
	}
	
	
	public LinkDirective(CodelistLink def) {
		this.def = def;
	}
	
	
	public CodelistLink definition() {
		return def;
	}
	
	
	public QName columnName() {
		return columnName==null?def.qname():columnName;
	}
	
	
	public LinkDirective to(QName name) {
		this.columnName=name;
		return this;
	}
	
	public LinkDirective to(String name) {
		return to(new QName(name));
	}
	
}
